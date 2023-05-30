package com.example.springjunitpractice.dto.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

public class AccountRespDto {
    
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Getter
    public static class AccountListRespDto {
        private String fullname;
        private List<AccountDto> accounts = new ArrayList<>();


        public AccountListRespDto(User user, List<Account> accounts) {
            this.fullname = user.getFullname();
            this.accounts = accounts.stream().map((account) -> new AccountDto(account)).collect(Collectors.toList());
        }

        @Getter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter
    public static class AccountDepositRespDto {
        private Long id;
        private Long number;
        private TransactionDto transaction;

        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String reciver;
            private Long amount;
            private String phone;
            private String created;

            @JsonIgnore // 클라이언트에게 전달하지 않도록 설정
            private Long depositAccountBalance; // 입금된 계좌의 총액 (서비스단 테스트 용도)

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.phone = transaction.getPhone();
                this.created = CustomDateUtil.toStringFormat(transaction.getCreated());
                this.depositAccountBalance = transaction.getDepositAccountBalance();
            }
        }
    }

    @Getter
    public static class AccountWithdrawRespDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public AccountWithdrawRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String reciver;
            private Long amount;
            private String created;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.created = CustomDateUtil.toStringFormat(transaction.getCreated());
            }
        }
    }
}
