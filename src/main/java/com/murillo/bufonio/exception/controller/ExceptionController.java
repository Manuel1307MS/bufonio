package com.murillo.bufonio.exception.controller;

import java.util.Map;

import com.murillo.bufonio.exception.dto.ExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/error")
public class ExceptionController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping
    public ResponseEntity<ExceptionResponse> handleError(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);

        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();

        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(webRequest, options);

        int status = (Integer) errorDetails.get("status");
        String error = (String) errorDetails.get("error");
        String message = (String) errorDetails.get("message");
        String path = (String) errorDetails.get("path");

        ExceptionResponse exceptionResponse = new ExceptionResponse(status, error, message, path, null);

        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }
}