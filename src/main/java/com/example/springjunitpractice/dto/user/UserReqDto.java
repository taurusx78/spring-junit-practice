package com.example.springjunitpractice.dto.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

import lombok.Data;

public class UserReqDto {

    @Data
    public static class JoinReqDto {

        // 유효성 검사 필요!
        private String username;
        private String password;
        private String email;
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
