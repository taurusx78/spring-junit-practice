package com.example.springjunitpractice.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.springjunitpractice.config.auth.PrincipalDetails;

// 인증된 사용자인지 확인하는 필터
// 모든 요청은 해당 필터를 거침

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("JwtAuthorizationFilter 인가 필터 실행");

        // Authorization 헤더가 있는지 확인
        String header = existHeader(request, response);

        if (header != null) {
            String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

            // 토큰 검증 (검증 실패 시 SignatureVerificationException 예외 발생)
            PrincipalDetails principal = JwtProcess.verify(token);

            // 임시 세션 생성 (권한 체크용)
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
                    principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    public String existHeader(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtProperties.HEADER);

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return null;
        }
        return header;
    }
}
