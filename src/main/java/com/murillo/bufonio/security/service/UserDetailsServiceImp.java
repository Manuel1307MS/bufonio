package com.murillo.bufonio.security.service;

import com.murillo.bufonio.model.User;
import com.murillo.bufonio.security.model.UserDetailsImp;
import com.murillo.bufonio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmailUser(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImp(user);
    }
}