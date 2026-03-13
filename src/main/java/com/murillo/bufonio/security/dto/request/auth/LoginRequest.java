package com.murillo.bufonio.security.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String emailUser,

        @NotBlank(message = "Password is required")
        String passwordUser
) {
}