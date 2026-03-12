package com.murillo.bufonio.service;

import com.murillo.bufonio.dto.summary.UserSummary;
import com.murillo.bufonio.exception.custom.RecourseNotFoundException;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.repository.UserRepository;
import com.murillo.bufonio.security.service.SecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityContextService securityContextService;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean existByEmailUser(String email) {
        return userRepository.existsByEmailUser(email);
    }

    public Optional<User> getUserByEmailUser(String email) {
        return userRepository.findUserByEmailUser(email);
    }

    public User getUserByIdUser(){
        User user = securityContextService.getCurrentUser().getUser();
        return userRepository.findUserByIdUser(user.getIdUser()).orElseThrow(()->new RecourseNotFoundException("User not found"));
    }

    public UserSummary getUserSummaryByIdUser() {
        User user = securityContextService.getCurrentUser().getUser();
        return userRepository.findUserSummaryByIdUser(user.getIdUser()).orElseThrow(()-> new RecourseNotFoundException("User not found"));
    }
}
