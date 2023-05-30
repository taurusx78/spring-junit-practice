package com.example.springjunitpractice.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev") // dev 모드에서만 실행되도록 설정
    @Bean
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository) { // 자동으로 DI 해줌
        return (args) -> {
            // 서버 실행 시 무조건 실행됨
            User user = userRepository.save(newUser("user1", "User"));
            accountRepository.save(newAccount(1111L, user));
        };
    }
}
