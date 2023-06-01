package com.example.springjunitpractice.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.domain.transaction.TransactionEnum;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

public class DummyObject {

    // 저장용
    protected static User newUser(String username, String fullname) {
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
    protected static User newMockUser(Long id, String username, String fullname) {
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
    protected static Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(1234)
                .balance(1000L)
                .user(user)
                .build();
    }

    // 조회용
    protected static Account newMockAccount(Long id, Long number, Long balance, User user) {
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
    protected static Transaction newMockDepositTransaction(Long id, Account account) {
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

    // 저장용
    protected static Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
        account.withdraw(100L); // 잔액 1000원 -> 900원

        // Repository 테스트에서는 변경된 account가 더티체킹으로 자동 update 되지만,
        // Controller 테스트에서는 더티체킹이 되지 않기 때문에 수동으로 update 해주어야 함!
        if (accountRepository != null) {
            accountRepository.save(account);
        }

        Transaction transaction = Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(account.getNumber().toString())
                .receiver("ATM")
                .build();
        return transaction;
    }

    // 저장용
    protected static Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
        account.deposit(100L); // 잔액 1000원 -> 1100원

        // Repository 테스트에서는 변경된 account가 더티체킹으로 자동 update 되지만,
        // Controller 테스트에서는 더티체킹이 되지 않기 때문에 수동으로 update 해주어야 함!
        if (accountRepository != null) {
            accountRepository.save(account);
        }

        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber().toString())
                .phone("01011111111")
                .build();
        return transaction;
    }

    // 저장용
    protected static Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount,
            AccountRepository accountRepository) {
        withdrawAccount.withdraw(100L); // 잔액 1000원 -> 900원
        depositAccount.deposit(100L); // 잔액 1000원 -> 1100원

        // Repository 테스트에서는 변경된 account가 더티체킹으로 자동 update 되지만,
        // Controller 테스트에서는 더티체킹이 되지 않기 때문에 수동으로 update 해주어야 함!
        if (accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }

        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccount.getNumber().toString())
                .receiver(depositAccount.getNumber().toString())
                .build();
        return transaction;
    }
}
