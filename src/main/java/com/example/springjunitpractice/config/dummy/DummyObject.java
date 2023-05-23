package com.example.springjunitpractice.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

public class DummyObject {

    // 저장용
    protected User newUser(String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username + "@naver.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .build();
    }

    // 조회용
    protected User newMockUser(Long id, String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .id(id)
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username + "@naver.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }

    // 저장용
    protected Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(1234)
                .balance(1000L)
                .user(user)
                .build();
    }

    // 조회용
    protected Account newMockAccount(Long id, Long number, Long balance, User user) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234)
                .balance(balance)
                .user(user)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }
}
