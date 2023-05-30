package com.example.springjunitpractice.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserEnum;

public class JwtProcessTest {

    private String createToken() {
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        PrincipalDetails principal = new PrincipalDetails(user);
        return JwtProcess.create(principal);
    }
    
    @Test
    public void create_test() throws Exception {
        // given

        // when
        String jwtToken = createToken();
        System.out.println("테스트: " + jwtToken);
        
        // then
        assertThat(jwtToken.startsWith(JwtProperties.TOKEN_PREFIX));
    }

    @Test
    public void verify_test() throws Exception {
        // given
        String token = createToken();
        String jwtToken = token.replace(JwtProperties.TOKEN_PREFIX, "");
    
        // when
        PrincipalDetails principal = JwtProcess.verify(jwtToken);
    
        // then
        assertThat(principal.getUser().getId()).isEqualTo(1L);
    }
}
