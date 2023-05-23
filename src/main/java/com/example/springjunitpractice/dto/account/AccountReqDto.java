package com.example.springjunitpractice.dto.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.user.User;

import lombok.Data;

public class AccountReqDto {

    @Data
    public static class AccountSaveReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 0)
        private int number;

        @NotNull
        @Digits(integer = 4, fraction = 0)
        private int password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }
    }
}
