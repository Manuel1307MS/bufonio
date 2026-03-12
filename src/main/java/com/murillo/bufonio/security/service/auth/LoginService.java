package com.murillo.bufonio.security.service.auth;

import com.murillo.bufonio.model.User;
import com.murillo.bufonio.security.model.UserDetailsImp;
import com.murillo.bufonio.security.service.JwtAccessTokenService;
import com.murillo.bufonio.security.service.JwtRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenService jwtAccessTokenService;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;

    public Map<String, String> login(String emailUser, String passwordUser) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        emailUser,
                        passwordUser
                )
        );

        UserDetailsImp userDetailsImp = (UserDetailsImp) auth.getPrincipal();
        User user = userDetailsImp.getUser();

        String accessToken = jwtAccessTokenService.generateAccessToken(user);
        String refreshToke = jwtRefreshTokenService.generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToke);

        return tokens;
    }
}