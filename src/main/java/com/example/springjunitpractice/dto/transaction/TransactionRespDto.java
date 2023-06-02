package com.example.springjunitpractice.dto.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.util.CustomDateUtil;

import lombok.Data;
import lombok.Getter;

public class TransactionRespDto {

    @Data
    public static class TransactionListRespDto {
        private List<TransactionDto> transactions = new ArrayList<>();

        public TransactionListRespDto(List<Transaction> transactions, Long accountNumber) {
            this.transactions = transactions.stream().map((t) -> new TransactionDto(t, accountNumber))
                    .collect(Collectors.toList());
        }

        @Getter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private Long amount;
            private String sender;
            private String reciver;
            private String phone;
            private String created;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReceiver();
                this.phone = transaction.getPhone() == null ? "없음" : transaction.getPhone();
                this.created = CustomDateUtil.toStringFormat(transaction.getCreated());

                if (transaction.getDepositAccount() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else if (transaction.getWithdrawAccount() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    if (transaction.getDepositAccount().getNumber().longValue() == accountNumber.longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }
    }
}
