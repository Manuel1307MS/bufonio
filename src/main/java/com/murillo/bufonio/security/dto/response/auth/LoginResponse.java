package com.murillo.bufonio.security.dto.response.auth;

import com.murillo.bufonio.dto.UserDTO;

public record LoginResponse(String accessToken, UserDTO userDTO) {
}