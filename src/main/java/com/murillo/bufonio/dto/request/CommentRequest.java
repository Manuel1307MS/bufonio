package com.murillo.bufonio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "El comentario no puede estar vacío")
        @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
        String comment
) {}