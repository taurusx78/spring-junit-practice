package com.example.springjunitpractice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountListRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;
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
    public void 계좌등록_test() throws Exception {
        // given
        Long userId = 1L;

        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234);

        // stub
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        // stub
        User user = newMockUser(userId, "user", "User");
        Account account = newMockAccount(1L, 1111L, 1000L, user);
        when(accountRepository.save(any())).thenReturn(account);

        // when
        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(userId, accountSaveReqDto);

        // then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(account.getNumber());
    }

    @Test
    public void 계좌목록보기_유저별_test() throws Exception {
        // given
        Long userId = 1L;
        String fullname = "User";

        // stub
        User user = newMockUser(userId, "user", fullname);
        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, user);
        Account ssarAccount2 = newMockAccount(2L, 2222L, 1000L, user);
        List<Account> accountList = Arrays.asList(ssarAccount1, ssarAccount2);
        when(accountRepository.findByUser_id(any())).thenReturn(accountList);

        // when
        AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(userId, fullname);

        // then
        assertThat(accountListRespDto.getFullname()).isEqualTo(fullname);
        assertThat(accountListRespDto.getAccounts().size()).isEqualTo(accountList.size());
    }

    @Test
    public void 계좌삭제_fail_test() throws Exception {
        // given
        Long number = 1111L;
        Long userId = 1L;

        // stub
        User user = newMockUser(2L, "user", "User");
        Account account = newMockAccount(1L, number, 1000L, user);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account));

        // when & then
        assertThrows(CustomApiException.class, () -> accountService.계좌삭제(number, userId));
    }
}
