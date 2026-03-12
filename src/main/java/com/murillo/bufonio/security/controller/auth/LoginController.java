package com.murillo.bufonio.security.controller.auth;

import com.murillo.bufonio.dto.UserDTO;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.service.UserService;
import com.murillo.bufonio.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.murillo.bufonio.security.dto.request.auth.LoginRequest;
import com.murillo.bufonio.security.dto.response.auth.LoginResponse;
import com.murillo.bufonio.security.service.auth.LoginService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Value("${jwt.refresh-token.time}")
    private long expirationTime;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        Map<String,String> tokens = loginService.login(loginRequest.emailUser(), loginRequest.passwordUser());

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(expirationTime / 1000)
                .sameSite("None")
                .build();

        User user = userService.getUserByEmailUser(loginRequest.emailUser()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserDTO userDTO = userMapper.toDTO(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(accessToken, userDTO));
    }
}