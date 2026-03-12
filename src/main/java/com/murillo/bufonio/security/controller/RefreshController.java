package com.murillo.bufonio.security.controller;

import com.murillo.bufonio.security.dto.RefreshResponse;
import com.murillo.bufonio.security.service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RefreshController {

    @Autowired
    private RefreshService refreshService;

    @GetMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        String accessToken = refreshService.getNewAccessToken(refreshToken);

        RefreshResponse refreshResponse = new RefreshResponse(accessToken);

        return ResponseEntity.ok(refreshResponse);
    }
}
