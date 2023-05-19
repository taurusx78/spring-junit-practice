package com.example.springjunitpractice.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.example.springjunitpractice.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomResponseUtil {

    public static void success(HttpServletResponse response, Object dto) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인 되었습니다.", dto);
        String responseBody = objectMapper.writeValueAsString(responseDto);
        response.getWriter().println(responseBody);
    }
    
    public static void fail(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(status);

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
        String responseBody = objectMapper.writeValueAsString(responseDto);
        response.getWriter().println(responseBody);
    }
}
