package com.example.springjunitpractice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.domain.transaction.TransactionEnum;
import com.example.springjunitpractice.domain.transaction.TransactionRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountWithdrawReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountDepositRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountDetailsRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountListRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountTransferRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountWithdrawRespDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountSaveRespDto 계좌등록(Long userId, AccountSaveReqDto accountSaveReqDto) {
        // 계좌 중복 여부 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());

        if (accountOP.isPresent()) {
            throw new CustomApiException("이미 등록된 계좌번호입니다.");
        }

        // 임시 User 객체 생성 (id 포함)
        User user = User.builder().id(userId).build();

        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(user));

        return new AccountSaveRespDto(accountPS);
    }

    public AccountListRespDto 계좌목록보기_유저별(Long userId, String fullname) {
        List<Account> accountListPS = accountRepository.findByUser_id(userId);

        // 임시 User 객체 생성 (id, fullname 포함)
        User user = User.builder().id(userId).fullname(fullname).build();

        return new AccountListRespDto(user, accountListPS);
    }

    @Transactional
    public void 계좌삭제(Long number, Long userId) {
        // 계좌번호 확인
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌가 존재하지 않습니다."));

        // 계좌 소유자 확인 (소유자 아닐 시 Exception 발생)
        accountPS.checkOwner(userId);

        // 계좌 삭제
        accountRepository.deleteById(accountPS.getId());
    }

    // ATM -> 계좌 (인증 필요 없음)
    @Transactional
    public AccountDepositRespDto 계좌입금(AccountDepositReqDto accountDepositReqDto) {
        // 입금 금액 0원 체크
        if (accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이상의 금액을 입금해 주세요.");
        }

        // 계좌 존재 유무 확인
        Account accountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌가 존재하지 않습니다."));

        // 입금
        accountPS.deposit(accountDepositReqDto.getAmount());

        // 거래 내역 기록
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(accountPS)
                .withdrawAccountBalance(null)
                .depositAccountBalance(accountPS.getBalance())
                .amount(accountDepositReqDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(accountDepositReqDto.getNumber().toString())
                .phone(accountDepositReqDto.getPhone())
                .build();
        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountDepositRespDto(accountPS, transactionPS);
    }

    // 나의 계좌 -> ATM
    @Transactional
    public AccountWithdrawRespDto 계좌출금(Long userId, AccountWithdrawReqDto accountWithdrawReqDto) {
        // 출금 금액 0원 체크
        if (accountWithdrawReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이상의 금액을 출금해 주세요.");
        }

        // 계좌 존재 유무 확인
        Account accountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌가 존재하지 않습니다."));

        // 출금계좌 소유자 확인 (로그인한 사용자와 동일한지)
        accountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        accountPS.checkPassword(accountWithdrawReqDto.getPassword());

        // 출금계좌 잔액 확인 및 출금
        accountPS.withdraw(accountWithdrawReqDto.getAmount());

        // 거래 내역 기록
        Transaction transaction = Transaction.builder()
                .withdrawAccount(accountPS)
                .depositAccount(null)
                .withdrawAccountBalance(accountPS.getBalance())
                .depositAccountBalance(null)
                .amount(accountWithdrawReqDto.getAmount())
                .gubun(TransactionEnum.WITHDRAW)
                .sender(accountWithdrawReqDto.getNumber().toString())
                .receiver("ATM")
                .build();
        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountWithdrawRespDto(accountPS, transactionPS);
    }

    // 나의 계좌 -> 타인 계좌
    @Transactional
    public AccountTransferRespDto 계좌이체(Long userId, AccountTransferReqDto accountTransferReqDto) {
        // 출금계좌와 입금계좌가 동일하면 안됨
        if (accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("입출금계좌가 동일할 수 없습니다.");
        }

        // 출금 금액 0원 체크
        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이상의 금액을 출금해 주세요.");
        }

        // 출금/입금계좌 존재 유무 확인
        Account withdrawAccountPS = accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber()).orElseThrow(
                () -> new CustomApiException("출금하려는 계좌가 존재하지 않습니다."));
        Account depositAccountPS = accountRepository.findByNumber(accountTransferReqDto.getDepositNumber()).orElseThrow(
                () -> new CustomApiException("입금하려는 계좌가 존재하지 않습니다."));

        // 출금계좌 소유자 확인 (로그인한 사용자와 동일한지)
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkPassword(accountTransferReqDto.getWithdrawPassword());

        // 출금계좌 잔액 확인 및 이체
        withdrawAccountPS.withdraw(accountTransferReqDto.getAmount());
        depositAccountPS.deposit(accountTransferReqDto.getAmount());

        // 거래 내역 기록
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountTransferReqDto.getAmount())
                .gubun(TransactionEnum.TRANSFER)
                .sender(accountTransferReqDto.getWithdrawNumber().toString())
                .receiver(accountTransferReqDto.getDepositNumber().toString())
                .build();
        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountTransferRespDto(withdrawAccountPS, transactionPS);
    }

    public AccountDetailsRespDto 계좌상세보기(Long number, Long userId, int page) {
        // 구분값 고정
        String gubun = "ALL";

        // 계좌 존재 유무 확인
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌가 존재하지 않습니다."));

        // 계좌 소유자 확인 (로그인한 사용자와 동일한지)
        accountPS.checkOwner(userId);

        // 입출금내역 조회
        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountPS.getId(), gubun, page);

        return new AccountDetailsRespDto(accountPS, transactionListPS);
    }
}
