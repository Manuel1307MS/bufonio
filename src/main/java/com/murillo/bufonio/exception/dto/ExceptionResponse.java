package com.murillo.bufonio.exception.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(int status, String error, String message, String path, List<String> errors) {
}
