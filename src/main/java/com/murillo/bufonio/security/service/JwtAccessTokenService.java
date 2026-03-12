package com.murillo.bufonio.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.murillo.bufonio.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtAccessTokenService {

    @Value("${jwt.access-token.secret}")
    private String secretKey;

    @Value("${jwt.access-token.time}")
    private long expirationTime;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getEmailUser())
                .withClaim("idUser", user.getIdUser())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(getAlgorithm());
    }

    public String getEmailFromAccessToken(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getSubject();
    }

    public Long getIdUserFromAccessToken(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getClaim("idUser")
                .asLong();
    }
}