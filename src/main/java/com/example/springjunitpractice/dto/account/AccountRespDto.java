package com.example.springjunitpractice.dto.account;

import com.example.springjunitpractice.domain.account.Account;

import lombok.Getter;

public class AccountRespDto {
    
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private int number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
}
