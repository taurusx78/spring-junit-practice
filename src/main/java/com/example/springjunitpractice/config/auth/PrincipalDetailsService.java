package com.example.springjunitpractice.config.auth;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 로그인 성공 시 세션 객체 생성됨
    // Session < SecurityContext < Authentication < UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new InternalAuthenticationServiceException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return new PrincipalDetails(user);
    }

}
