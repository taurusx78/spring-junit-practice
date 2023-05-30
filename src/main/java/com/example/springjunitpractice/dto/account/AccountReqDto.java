package com.example.springjunitpractice.dto.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.user.User;

import lombok.Data;

public class AccountReqDto {

    @Data
    public static class AccountSaveReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 0)
        private Long number;

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

    @Data
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 0)
        private Long number;

        @NotNull
        private Long amount; // 입금 금액

        @NotBlank
        @Pattern(regexp = "^(DEPOSIT)$")
        private String gubun; // DEPOSIT

        @NotBlank
        @Pattern(regexp = "^(01)[0-9]{9}$")
        private String phone;
    }

    @Data
    public static class AccountWithdrawReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 0)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 0)
        private int password;

        @NotNull
        private Long amount; // 입금 금액

        @NotBlank
        @Pattern(regexp = "^(WITHDRAW)$")
        private String gubun; // WITHDRAW
    }

    @Data
    public static class AccountTransferReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 0)
        private Long withdrawNumber; // 출금계좌

        @NotNull
        @Digits(integer = 20, fraction = 0)
        private Long depositNumber; // 입금계좌

        @NotNull
        @Digits(integer = 4, fraction = 0)
        private int withdrawPassword;

        @NotNull
        private Long amount; // 입금 금액

        @NotBlank
        @Pattern(regexp = "^(TRANSFER)$")
        private String gubun; // TRANSFER
    }
}
