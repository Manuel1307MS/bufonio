package com.murillo.bufonio.security.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginGoogleRequest(
        @NotBlank(message = "idToken is required")
        String idToken
) {}