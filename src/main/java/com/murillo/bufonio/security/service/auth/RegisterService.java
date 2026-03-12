package com.murillo.bufonio.security.service.auth;

import com.murillo.bufonio.exception.custom.EmailAlreadyRegisteredException;
import com.murillo.bufonio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.murillo.bufonio.model.User;

@Service
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {

        boolean existUser = userService.existByEmailUser(user.getEmailUser());

        if (existUser) throw new EmailAlreadyRegisteredException("El email ya está registrado");

        String encodedPassword = passwordEncoder.encode(user.getPasswordUser());
        user.setPasswordUser(encodedPassword);

        return userService.createUser(user);
    }
}
