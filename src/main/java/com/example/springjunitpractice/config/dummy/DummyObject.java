package com.example.springjunitpractice.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.domain.transaction.TransactionEnum;
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

    // 조회용
    protected Transaction newMockDepositTransaction(Long id, Account account) {
        // 계좌 잔액 1100L으로 변경
        account.deposit(100L);

        Transaction transaction = Transaction.builder()
                .id(id)
                .depositAccount(account)
                .withdrawAccount(null)
                .depositAccountBalance(account.getBalance())
                .withdrawAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber().toString())
                .phone("01011111111")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        return transaction;
    }
}
