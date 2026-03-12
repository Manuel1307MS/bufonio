package com.murillo.bufonio.controller;

import com.murillo.bufonio.dto.UserDTO;
import com.murillo.bufonio.dto.summary.UserSummary;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.service.UserService;
import com.murillo.bufonio.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User user = userService.getUserByIdUser();
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/me/summary")
    public ResponseEntity<UserSummary> getCurrentUserSummary() {
        UserSummary userSummary = userService.getUserSummaryByIdUser();
        return ResponseEntity.ok(userSummary);
    }
}