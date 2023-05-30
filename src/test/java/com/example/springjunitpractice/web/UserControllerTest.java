package com.example.springjunitpractice.web;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.user.UserReqDto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

// @Transactional // 하나의 테스트가 완료되면 데이터를 Rollback 함
@Sql("classpath:db/teardown.sql") // 하나의 테스트가 완료되면 teardown.sql 쿼리 실행
@AutoConfigureMockMvc // Mockito 환경에 MockMvc 빈 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mockito(가짜) 환경에서 테스트
public class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("user1", "User"));
        entityManager.clear(); // 정확한 테스트를 위해 영속성 컨텍스트 초기화
    }

    @Test
    public void join_success_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("user2");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("user2@naver.com");
        joinReqDto.setFullname("User");

        // DTO 객체를 Json 데이터로 변환
        String requestBody = objectMapper.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        
        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("user1");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("user1@naver.com");
        joinReqDto.setFullname("User");

        // DTO 객체를 Json 데이터로 변환
        String requestBody = objectMapper.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        
        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
