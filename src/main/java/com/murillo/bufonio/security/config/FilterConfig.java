package com.murillo.bufonio.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murillo.bufonio.security.filter.JwtAuthenticationFilter;
import com.murillo.bufonio.security.filter.RateLimitFilter;
import com.murillo.bufonio.security.service.JwtAccessTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RateLimitFilter rateLimitFilter(ObjectMapper objectMapper) {
        return new RateLimitFilter(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAccessTokenService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }
}