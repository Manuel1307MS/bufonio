package com.murillo.bufonio.exception;

import java.util.List;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.murillo.bufonio.exception.custom.*;
import com.murillo.bufonio.exception.dto.ExceptionResponse;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // --- HELPER ---
        @Nonnull
        private ResponseEntity<ExceptionResponse> buildResponse(@Nonnull HttpStatus status, String message, HttpServletRequest request, List<String> errors) {
                ExceptionResponse response = new ExceptionResponse(
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        request.getRequestURI(),
                        errors
                );
                return ResponseEntity.status(status).body(response);
        }

        // --- CUSTOM ---

        @ExceptionHandler(UnauthenticatedException.class)
        public ResponseEntity<ExceptionResponse> handleUnauthenticated(UnauthenticatedException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request, null);
        }

        @ExceptionHandler(EmailAlreadyRegisteredException.class)
        public ResponseEntity<ExceptionResponse> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, null);
        }

        @ExceptionHandler(RecourseNotFoundException.class)
        public ResponseEntity<ExceptionResponse> handleResourceNotFound(RecourseNotFoundException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
        }

        // --- VALIDATION EXCEPTIONS ---

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
                List<String> errors = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .toList();

                return buildResponse(HttpStatus.BAD_REQUEST, "Validation error in request fields", request, errors);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
                List<String> errors = ex.getConstraintViolations()
                        .stream()
                        .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                        .toList();

                return buildResponse(HttpStatus.BAD_REQUEST, "Constraint violation error", request, errors);
        }

        // --- TECHNICAL EXCEPTIONS ---

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {

                Throwable cause = ex.getCause();

                if (cause instanceof UnrecognizedPropertyException unrecognized) {
                        String detail = "The field '" + unrecognized.getPropertyName() + "' is not recognized.";
                        return buildResponse(HttpStatus.BAD_REQUEST, "Extra field detected", request, List.of(detail));
                }

                if (cause instanceof InvalidFormatException invalid) {
                        String fieldName = invalid.getPath().getFirst().getFieldName();
                        String detail = "The field '" + fieldName + "' received an invalid value: '" + invalid.getValue() + "'.";
                        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid format", request, List.of(detail));
                }

                return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON", request, null);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed: " + ex.getMethod(), request, null);
        }

        // --- JWT ---

        @ExceptionHandler(JWTVerificationException.class)
        public ResponseEntity<ExceptionResponse> handleJWTVerification(JWTVerificationException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid RefreshToken", request, null);
        }

        @ExceptionHandler(TokenExpiredException.class)
        public ResponseEntity<ExceptionResponse> handleTokenExpired(TokenExpiredException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.UNAUTHORIZED, "The RefreshToken has expired", request, null);
        }

        // --- LOGIN ---

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", request, List.of("The username or password does not match our records."));
        }

        // --- ALL ---

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ExceptionResponse> handleAll(Exception ex, HttpServletRequest request) {
                return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, null);
        }
}