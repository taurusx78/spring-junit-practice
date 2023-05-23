package com.example.springjunitpractice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.user.UserReqDto.JoinReqDto;
import com.example.springjunitpractice.dto.user.UserRespDto.JoinRespDto;

// Mockito 환경: 스프링 관련 빈들이 하나도 없는 가짜 환경

@ExtendWith(MockitoExtension.class) // Mockito 환경에서 테스트
public class UserServiceTest extends DummyObject {

    // 모든 Mock 객체가 InjectMocks 객체에 주입됨
    @InjectMocks
    private UserService userService;

    // 가짜 객체 생성
    @Mock
    private UserRepository userRepository;

    // 진짜 객체를 InjectMocks 객체에 주입
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    
    @Test
    public void 회원가입_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("user");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("user@naver.com");
        joinReqDto.setFullname("User");

        // stub 1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // stub 2
        User user = newMockUser(1L, "user", "User");
        when(userRepository.save(any())).thenReturn(user);
    
        // when
        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
    
        // then
        assertThat(joinRespDto.getUsername()).isEqualTo(joinReqDto.getUsername());
    }
}
