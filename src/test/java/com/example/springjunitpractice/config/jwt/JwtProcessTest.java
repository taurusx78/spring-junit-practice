package com.example.springjunitpractice.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

public class JwtProcessTest {
    
    @Test
    public void create_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        PrincipalDetails principal = new PrincipalDetails(user);

        // when
        String jwtToken = JwtProcess.create(principal);
        System.out.println("테스트: " + jwtToken);
        
        // then
        assertThat(jwtToken.startsWith(JwtProperties.TOKEN_PREFIX));
    }

    @Test
    public void verify_test() throws Exception {
        // given
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqd3QiLCJyb2xlIjoiQ1VTVE9NRVIiLCJpZCI6MSwiZXhwIjoxNjg1MzI3MjYwfQ.jBDmAiLClLGkaU1C5MKKANTzSaTt8tH8tNHUE6BmxyCfVvdpRnBUmY7ttY1poWjxKqIeE4cXh3j4anBiz-lTrw";
    
        // when
        PrincipalDetails principal = JwtProcess.verify(jwtToken);
    
        // then
        assertThat(principal.getUser().getId()).isEqualTo(1L);
    }
}
