package com.example.springjunitpractice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountListRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {
    
    private final AccountRepository accountRepository;

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
            () -> new CustomApiException("계좌가 존재하지 않습니다.")
        );

        // 계좌 소유자 확인 (소유자 아닐 시 Exception 발생)
        accountPS.checkOwner(userId);

        // 계좌 삭제
        accountRepository.deleteById(accountPS.getId());
    }
}
