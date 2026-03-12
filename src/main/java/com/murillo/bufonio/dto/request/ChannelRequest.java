package com.murillo.bufonio.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChannelRequest(@NotBlank(message = "El nombre del canal no puede estar vacío") String nameChannel) {
}