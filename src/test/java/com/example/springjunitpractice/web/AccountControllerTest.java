package com.example.springjunitpractice.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional // 하나의 테스트가 완료되면 데이터를 Rollback 함
@AutoConfigureMockMvc // Mockito 환경에 MockMvc 빈 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mockito(가짜) 환경에서 테스트
public class AccountControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("user1", "User"));
    }

    // 강제로 시큐리티 세션 생성 (DB에서 username = user 조회 후 세션에 담음)
    // setupBefore=TEST_METHOD : setUp() 메서드 실행전 세션 DB 조회
    // setupBefore=TEST_EXECUTION : save_account_test() 메서드 실행전 DB 조회
    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void save_account_test() throws Exception {
        // given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111);
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
}
