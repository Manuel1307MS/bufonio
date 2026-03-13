package com.murillo.bufonio.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChannelRequest(
        @NotBlank(message = "Channel name cannot be empty")
        String nameChannel
) {
}