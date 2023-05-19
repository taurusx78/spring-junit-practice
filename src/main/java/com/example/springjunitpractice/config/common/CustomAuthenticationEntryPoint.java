package com.example.springjunitpractice.config.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.example.springjunitpractice.util.CustomResponseUtil;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 익명 사용자가 인증이 필요한 자원에 접근했을 경우 호출될 메서드
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        System.out.println("CustomAuthenticationEntryPoint 진입");
        CustomResponseUtil.fail(response, "로그인을 진행해 주세요.", HttpStatus.UNAUTHORIZED.value());
    }

}
