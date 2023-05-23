package com.example.springjunitpractice.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;
import com.example.springjunitpractice.dto.user.UserReqDto.JoinReqDto;
import com.example.springjunitpractice.dto.user.UserRespDto.JoinRespDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JoinRespDto 회원가입(JoinReqDto joinReqDto) {
        // 1. 아이디 중복 검사
        Optional<User> userOP = userRepository.findByUsername(joinReqDto.getUsername());

        if (userOP.isPresent()) {
            throw new CustomApiException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 인코딩 + 회원가입
        User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));

        // 3. DTO 리턴
        return new JoinRespDto(userPS);
    }
}
