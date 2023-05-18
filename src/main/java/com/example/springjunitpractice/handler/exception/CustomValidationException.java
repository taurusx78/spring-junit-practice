package com.example.springjunitpractice.handler.exception;

import java.util.Map;

import lombok.Getter;

@Getter
public class CustomValidationException extends RuntimeException {
    
    private Map<String, String> errorMap;

    public CustomValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
