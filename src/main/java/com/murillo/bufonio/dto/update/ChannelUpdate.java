package com.murillo.bufonio.dto.update;

import jakarta.validation.constraints.NotBlank;

public record ChannelUpdate(
        @NotBlank(message = "Channel name cannot be empty")
        String nameChannel
) {
}