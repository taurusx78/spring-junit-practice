package com.example.springjunitpractice.config.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

public class JwtProcess {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 토큰 생성
    public static String create(PrincipalDetails principal) {
        String jwtToken = JWT.create()
                .withSubject("jwt")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principal.getUser().getId())
                .withClaim("role", principal.getUser().getRole().name())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return JwtProperties.TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (검증 실패 시 Exception 발생)
    public static PrincipalDetails verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
        PrincipalDetails principal = new PrincipalDetails(user);
        return principal;
    }
}
