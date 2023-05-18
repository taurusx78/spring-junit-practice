package com.example.springjunitpractice.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

import lombok.Data;

public class UserReqDto {

    @Data
    public static class JoinReqDto {

        // 영문, 숫자 4~16자
        @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "4~16자의 영문과 숫자로 구성해 주세요.")
        @NotBlank
        private String username;

        @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "4~16자의 영문과 숫자로 구성해 주세요.")
        @NotBlank
        private String password;

        @Pattern(regexp = "^[a-zA-Z0-9]{4,16}@[a-zA-Z0-9]{2,16}\\.[a-zA-Z]{2,3}$", message = "올바른 이메일 형식을 입력해 주세요.")
        @NotBlank
        private String email;

        @Pattern(regexp = "^[a-zA-Z가-힣]{2,20}$", message = "2~20자의 한글 또는 영문으로 구성해 주세요.")
        @NotBlank
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
