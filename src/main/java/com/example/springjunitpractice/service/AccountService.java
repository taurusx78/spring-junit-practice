package com.example.springjunitpractice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {
    
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountSaveRespDto saveAccount(Long userId, AccountSaveReqDto accountSaveReqDto) {
        // User가 DB에 있는지 검증
        User userPS = userRepository.findById(userId).orElseThrow(
            () -> new CustomApiException("존재하지 않는 사용자입니다.")
        );

        // 계좌 중복 여부 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());

        if (accountOP.isPresent()) {
            throw new CustomApiException("이미 등록된 계좌번호입니다.");
        }

        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));

        return new AccountSaveRespDto(accountPS);
    }
}
