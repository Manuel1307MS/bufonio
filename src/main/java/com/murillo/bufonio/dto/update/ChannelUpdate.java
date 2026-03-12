package com.murillo.bufonio.dto.update;

import jakarta.validation.constraints.NotBlank;

public record ChannelUpdate(@NotBlank(message = "El nombre del canal no puede estar vacío") String nameChannel) {
}