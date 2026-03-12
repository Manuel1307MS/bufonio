package com.murillo.bufonio.security.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo electrónico es obligatorio") @Email(message = "Correo electrónico inválido") String emailUser,
        @NotBlank(message = "La contraseña es obligatoria") String passwordUser) {
}