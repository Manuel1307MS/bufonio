package com.murillo.bufonio.security.service;

import com.murillo.bufonio.exception.custom.RecourseNotFoundException;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAccessTokenService jwtAccessTokenService;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;

    public String getNewAccessToken(String refreshToken) {

        User user = getUserFromRefreshToken(refreshToken);

        return jwtAccessTokenService.generateAccessToken(user);
    }

    public User getUserFromRefreshToken(String refreshToken) {
        String email = jwtRefreshTokenService.getEmailFromRefreshToken(refreshToken);
        return userService.getUserByEmailUser(email)
                .orElseThrow(()->new RecourseNotFoundException("Usuario no encontrado"));
    }
}