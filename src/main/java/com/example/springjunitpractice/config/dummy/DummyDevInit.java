package com.example.springjunitpractice.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.springjunitpractice.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {
    
    @Profile("dev") // dev 모드에서만 실행되도록 설정
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return (args) -> {
            // 서버 실행 시 무조건 실행됨
            userRepository.save(newUser("user", "User"));
        };
    }
}
