package com.example.springjunitpractice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class) // Mockito 환경에서 테스트
public class AccountServiceTest extends DummyObject {

    // 모든 Mock 객체가 InjectMocks 객체에 주입됨
    @InjectMocks
    private AccountService accountService;

    // 가짜 생성
    @Mock
    private UserRepository userRepository;

    // 가짜 생성
    @Mock
    private AccountRepository accountRepository;

    // 진짜 객체를 InjectMocks 객체에 주입
    @Spy
    private ObjectMapper objectMapper;
    
    @Test
    public void save_account_test() throws Exception {
        // given
        Long userId = 1L;

        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111);
        accountSaveReqDto.setPassword(1234);

        // stub 1
        User user = newMockUser(userId, "user", "User");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // stub 2
        when(accountRepository.findByNumber(anyInt())).thenReturn(Optional.empty());
    
        // stub 3
        Account account = newMockAccount(1L, 1111, 1000L, user);
        when(accountRepository.save(any())).thenReturn(account);

        // when
        AccountSaveRespDto accountSaveRespDto = accountService.saveAccount(userId, accountSaveReqDto);

        // then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(account.getNumber());
    }
}
