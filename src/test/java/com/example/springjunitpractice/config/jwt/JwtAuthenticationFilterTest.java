package com.example.springjunitpractice.config.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.user.UserReqDto.LoginReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional // 하나의 테스트가 완료되면 데이터를 Rollback 함
@AutoConfigureMockMvc // Mockito 환경에 MockMvc 빈 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mockito(가짜) 환경에서 테스트
public class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("user", "User"));
    }

    @Test
    public void successfulAuthentication_test() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("user");
        loginReqDto.setPassword("1234");
        // DTO 객체를 JSON 형식으로 변환
        String requestBody = objectMapper.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtProperties.HEADER);
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtProperties.TOKEN_PREFIX));
        resultActions.andExpect(jsonPath("$.data.username").value("user"));
    }

    @Test
    public void unsuccessfulAuthentication_test() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("user");
        loginReqDto.setPassword("12345");
        // DTO 객체를 JSON 형식으로 변환
        String requestBody = objectMapper.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isUnauthorized());
    }
}
