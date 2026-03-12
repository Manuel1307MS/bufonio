package com.murillo.bufonio.security.service;

import com.murillo.bufonio.exception.custom.UnauthenticatedException;
import com.murillo.bufonio.security.model.UserDetailsImp;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public UserDetailsImp getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new UnauthenticatedException("Credenciales no validas");
        }
        return (UserDetailsImp) auth.getPrincipal();
    }
}