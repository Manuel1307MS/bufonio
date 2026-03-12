package com.murillo.bufonio.security.controller.auth;

import com.murillo.bufonio.dto.UserDTO;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.util.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.murillo.bufonio.security.dto.request.auth.RegisterRequest;
import com.murillo.bufonio.security.dto.response.auth.RegisterResponse;
import com.murillo.bufonio.security.service.auth.RegisterService;
import com.murillo.bufonio.util.LocationUtil;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        User user = userMapper.fromRegisterRequest(registerRequest);
        User userRegistered = registerService.register(user);
        UserDTO userDTO = userMapper.toDTO(userRegistered);
        RegisterResponse registerResponse = new RegisterResponse(userDTO);
        URI location = LocationUtil.getLocation("/usuarios", registerResponse.userDTO().idUser());
        return ResponseEntity.created(location).body(registerResponse);
    }
}
