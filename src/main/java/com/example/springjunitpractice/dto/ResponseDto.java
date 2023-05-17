package com.example.springjunitpractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    
    private final Integer code;
    private final String message;
    private final T data;
}
