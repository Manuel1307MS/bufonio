package com.murillo.bufonio.exception.custom;

public class GoogleAuthenticationRequiredException extends RuntimeException {

    public GoogleAuthenticationRequiredException() {
        super("This account uses Google authentication. Please sign in with Google.");
    }

    public GoogleAuthenticationRequiredException(String message) {
        super(message);
    }
}