package com.example.springjunitpractice.config.jwt;

public interface JwtProperties {

    public static final String SECRET = "taurusx"; // HS256 대칭키 암호화 (토큰 생성과 검증을 서버측에서 하기 때문에 대칭키로 암호화 가능)
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
}
