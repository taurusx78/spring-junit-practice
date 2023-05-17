package com.example.springjunitpractice.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.example.springjunitpractice.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomResponseUtil {
    
    public static void unAuthorized(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
        String responseBody = objectMapper.writeValueAsString(responseDto);
        response.getWriter().println(responseBody);
    }
}
