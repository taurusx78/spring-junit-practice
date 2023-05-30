package com.example.springjunitpractice.web;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountWithdrawReqDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

// @Transactional // 하나의 테스트가 완료되면 데이터를 Rollback 함
@Sql("classpath:db/teardown.sql") // 하나의 테스트가 완료되면 teardown.sql 쿼리 실행
@AutoConfigureMockMvc // Mockito 환경에 MockMvc 빈 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mockito(가짜) 환경에서 테스트
public class AccountControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        User user1 = userRepository.save(newUser("user1", "User1"));
        User user2 = userRepository.save(newUser("user2", "User2"));
        accountRepository.save(newAccount(1111L, user1));
        accountRepository.save(newAccount(2222L, user2));
        entityManager.clear(); // 정확한 테스트를 위해 영속성 컨텍스트 초기화
    }

    // 강제로 시큐리티 세션 생성 (DB에서 username = user1 조회 후 세션에 담음)
    // setupBefore=TEST_METHOD : setUp() 메서드 실행전 세션 DB 조회
    // setupBefore=TEST_EXECUTION : saveAccount_test() 메서드 실행전 DB 조회
    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void saveAccount_test() throws Exception {
        // given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(2222L);
        accountSaveReqDto.setPassword(1234);

        // DTO 객체를 Json 데이터로 변환
        String requestBody = objectMapper.writeValueAsString(accountSaveReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findUserAccounts_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/account/login-user"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void deleteAccount_test() throws Exception {
        // given
        Long number = 1111L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/account/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        assertThrows(CustomApiException.class,
                () -> accountRepository.findByNumber(number)
                        .orElseThrow(() -> new CustomApiException("계좌가 존재하지 않습니다.")));
    }

    @Test
    public void depositAccount_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setPhone("01011111111");

        // DTO 객체를 Json 데이터로 변환
        String requestBody = objectMapper.writeValueAsString(accountDepositReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/account/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void withdrawAccount_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setGubun("WITHDRAW");

        // DTO 객체를 Json 데이터로 변환
        String requestBody = objectMapper.writeValueAsString(accountWithdrawReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/s/account/withdraw").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void transferAccount_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        // DTO 객체를 Json 데이터로 변환
        String requestBody = objectMapper.writeValueAsString(accountTransferReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/s/account/transfer").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }
}
