package com.murillo.bufonio.security.filter;

import com.murillo.bufonio.model.User;
import com.murillo.bufonio.security.model.UserDetailsImp;
import com.murillo.bufonio.security.service.JwtAccessTokenService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAccessTokenService jwtAccessTokenService;

    public JwtAuthenticationFilter(JwtAccessTokenService jwtAccessTokenService) {
        this.jwtAccessTokenService = jwtAccessTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Long idUser = jwtAccessTokenService.getIdUserFromAccessToken(token);
                String emailUser = jwtAccessTokenService.getEmailFromAccessToken(token);

                User user = new User();
                user.setIdUser(idUser);
                user.setEmailUser(emailUser);

                UserDetailsImp userDetailsImp = new UserDetailsImp(user);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetailsImp, null, userDetailsImp.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}