package com.murillo.bufonio.security.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio") String nameUser,
        @Email(message = "Correo electrónico inválido") @NotBlank(message = "El correo electrónico es obligatorio") String emailUser,
        @NotBlank(message = "La contraseña es obligatoria") String passwordUser) {
}