package com.example.springjunitpractice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.domain.transaction.TransactionRepository;
import com.example.springjunitpractice.dto.transaction.TransactionRespDto.TransactionListRespDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionListRespDto 입출금내역보기(Long userId, Long accountNumber, String gubun, int page) {
        // 계좌 존재 여부 확인
        Account accountPS = accountRepository.findByNumber(accountNumber).orElseThrow(() -> {
            throw new CustomApiException("계좌가 존재하지 않습니다.");
        });

        // 계좌 소유자 확인 (로그인한 사용자와 동일한지)
        accountPS.checkOwner(userId);

        // 입출금내역 조회
        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountPS.getId(), gubun, page);

        return new TransactionListRespDto(transactionListPS, accountNumber);
    }
}
