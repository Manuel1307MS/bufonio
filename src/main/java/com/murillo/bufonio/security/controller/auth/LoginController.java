package com.murillo.bufonio.security.controller.auth;

import com.murillo.bufonio.dto.UserDTO;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.service.UserService;
import com.murillo.bufonio.util.mapper.UserMapper;
import com.murillo.bufonio.security.dto.request.auth.LoginRequest;
import com.murillo.bufonio.security.dto.request.auth.LoginGoogleRequest; // El nuevo DTO
import com.murillo.bufonio.security.dto.response.auth.LoginResponse;
import com.murillo.bufonio.security.service.auth.LoginService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
        Map<String, String> tokens = loginService.login(loginRequest.emailUser(), loginRequest.passwordUser());
        return createLoginResponse(tokens);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginGoogle(@RequestBody @Valid LoginGoogleRequest loginGoogleRequest) {
        Map<String, String> tokens = loginService.loginGoogle(loginGoogleRequest.idToken());
        return createLoginResponse(tokens);
    }

    private ResponseEntity<LoginResponse> createLoginResponse(Map<String, String> tokens) {
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        String email = tokens.get("email");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(expirationTime / 1000)
                .sameSite("None")
                .build();

        User user = userService.getUserByEmailUser(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDTO userDTO = userMapper.toDTO(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(accessToken, userDTO));
    }
}