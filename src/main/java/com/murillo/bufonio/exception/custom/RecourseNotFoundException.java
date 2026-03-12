package com.murillo.bufonio.exception.custom;

public class RecourseNotFoundException extends RuntimeException{
    public RecourseNotFoundException(String mensaje){
        super(mensaje);
    }
}
