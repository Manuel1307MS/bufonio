package com.murillo.bufonio.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murillo.bufonio.exception.dto.ExceptionResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final Map<String, BucketData> cache = new ConcurrentHashMap<>();
    private static final Duration IP_EXPIRATION = Duration.ofMinutes(20);

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private record BucketData(Bucket bucket, Instant lastAccess) {}

    private Bucket resolveBucket(String ip) {
        if (cache.size() > 1000) {
            cache.entrySet().removeIf(entry ->
                    entry.getValue().lastAccess().plus(IP_EXPIRATION).isBefore(Instant.now())
            );
        }

        return cache.compute(ip, (key, existing) -> {
            Bucket bucket = (existing == null) ? createNewBucket() : existing.bucket();
            return new BucketData(bucket, Instant.now());
        }).bucket();
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(50)
                .refillGreedy(20, Duration.ofMinutes(1))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String ip = getClientIP(request);
        Bucket bucket = resolveBucket(ip);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        ExceptionResponse body = new ExceptionResponse(
                status.value(),
                status.getReasonPhrase(),
                "Too many requests.",
                request.getRequestURI(),
                null
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), body);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isBlank()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}