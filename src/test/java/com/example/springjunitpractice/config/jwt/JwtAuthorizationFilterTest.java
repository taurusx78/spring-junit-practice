package com.example.springjunitpractice.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

@AutoConfigureMockMvc // Mockito 환경에 MockMvc 빈 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mockito(가짜) 환경에서 테스트
public class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void authorization_success_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        PrincipalDetails principal = new PrincipalDetails(user);
        String jwtToken = JwtProcess.create(principal);
    
        // when
        ResultActions resultActions = mvc.perform(get("/api/s").header(JwtProperties.HEADER, jwtToken));
    
        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void authorization_fail_test() throws Exception {
        // given
    
        // when
        ResultActions resultActions = mvc.perform(get("/api/s"));
    
        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void authorization_admin_success_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.ADMIN).build();
        PrincipalDetails principal = new PrincipalDetails(user);
        String jwtToken = JwtProcess.create(principal);
    
        // when
        ResultActions resultActions = mvc.perform(get("/api/admin").header(JwtProperties.HEADER, jwtToken));
    
        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void authorization_admin_fail_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        PrincipalDetails principal = new PrincipalDetails(user);
        String jwtToken = JwtProcess.create(principal);
    
        // when
        ResultActions resultActions = mvc.perform(get("/api/admin").header(JwtProperties.HEADER, jwtToken));
    
        // then
        resultActions.andExpect(status().isForbidden());
    }
}
