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
import com.example.springjunitpractice.domain.transaction.Transaction;
import com.example.springjunitpractice.domain.transaction.TransactionRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountDepositRespDto;
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

    // 가짜 생성
    @Mock
    private TransactionRepository transactionRepository;

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

    @Test
    public void 계좌입금_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setPhone("01011111111");

        // stub 1
        User user1 = newMockUser(1L, "user", "User");
        Account account1 = newMockAccount(1L, 1111L, 1000L, user1);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account1));

        // stub 2
        User user2 = newMockUser(1L, "user", "User");
        Account account2 = newMockAccount(1L, 1111L, 1000L, user2);
        Transaction transaction = newMockDepositTransaction(1L, account2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);

        // then
        assertThat(account1.getBalance()).isEqualTo(accountDepositRespDto.getTransaction().getDepositAccountBalance());
    }

    @Test
    public void 계좌출금_test() throws Exception {
        // given
        Long amount = 100L;
        Long userId = 1L;
        int password = 1234;

        User user = newMockUser(userId, "user", "User");
        Account account = newMockAccount(1L, 1111L, 1000L, user);

        // when
        if (amount <= 0L) {
            throw new CustomApiException("0원 이상의 금액을 출금해 주세요.");
        }
        account.checkOwner(userId);
        account.checkPassword(password);
        account.withdraw(amount);

        // then
        assertThat(account.getBalance()).isEqualTo(900L);
    }

    @Test
    public void 계좌이체_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        User user1 = newMockUser(1000L, "user1", "User1");
        User user2 = newMockUser(2000L, "user2", "User2");
        Account withdrawAccount = newMockAccount(1L, 1111L, 1000L, user1);
        Account depositAccount = newMockAccount(2L, 2222L, 1000L, user2);

        // when
        // 출금계좌와 입금계좌가 동일하면 안됨
        if (accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber()
                .longValue()) {
            throw new CustomApiException("입출금계좌가 동일할 수 없습니다.");
        }

        // 0원 체크
        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이상의 금액을 출금해 주세요.");
        }

        // 출금계좌 소유자 확인 (로그인한 사용자와 동일한지)
        withdrawAccount.checkOwner(user1.getId());

        // 출금계좌 비밀번호 확인
        withdrawAccount.checkPassword(accountTransferReqDto.getWithdrawPassword());

        // 출금계좌 잔액 확인 및 이체
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());
        depositAccount.deposit(accountTransferReqDto.getAmount());

        // then
        assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
        assertThat(depositAccount.getBalance()).isEqualTo(1100L);
    }
}
