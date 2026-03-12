package com.murillo.bufonio.exception.custom;

public class UnauthenticatedException extends RuntimeException{
    public UnauthenticatedException(String mensaje) {
        super(mensaje);
    }
}