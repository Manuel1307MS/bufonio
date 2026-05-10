package com.murillo.bufonio.security.service.auth;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.google.auth.oauth2.TokenVerifier.VerificationException;
import com.murillo.bufonio.exception.custom.GoogleAuthenticationRequiredException;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.security.model.UserDetailsImp;
import com.murillo.bufonio.security.service.JwtAccessTokenService;
import com.murillo.bufonio.security.service.JwtRefreshTokenService;
import com.murillo.bufonio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private UserService userService;

    @Value("${google.client-id}")
    private String googleClientId;

    public Map<String, String> login(String emailUser, String passwordUser) {

        User user = userService.getUserByEmailUser(emailUser)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getPasswordUser() == null) {
            throw new GoogleAuthenticationRequiredException();
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailUser, passwordUser)
        );

        UserDetailsImp userDetailsImp = (UserDetailsImp) auth.getPrincipal();
        return generateTokensMap(userDetailsImp.getUser());
    }

    public Map<String, String> loginGoogle(String idTokenString) {
        try {
            TokenVerifier verifier = TokenVerifier.newBuilder()
                    .setAudience(googleClientId)
                    .setIssuer("https://accounts.google.com")
                    .build();

            JsonWebSignature jws = verifier.verify(idTokenString);
            JsonWebSignature.Payload payload = jws.getPayload();

            String email = (String) payload.get("email");
            String name = (String) payload.get("name");

            User user = userService.getUserByEmailUser(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmailUser(email);
                        newUser.setNameUser(name);
                        newUser.setPasswordUser(null);
                        return userService.createUser(newUser);
                    });

            return generateTokensMap(user);

        } catch (VerificationException e) {
            throw new RuntimeException("Google's token verification failed.");
        } catch (Exception e) {
            throw new RuntimeException("Google authentication failed" + e.getMessage());
        }
    }

    private Map<String, String> generateTokensMap(User user) {
        String accessToken = jwtAccessTokenService.generateAccessToken(user);
        String refreshToken = jwtRefreshTokenService.generateRefreshToken(user);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("email", user.getEmailUser());
        return tokens;
    }
}