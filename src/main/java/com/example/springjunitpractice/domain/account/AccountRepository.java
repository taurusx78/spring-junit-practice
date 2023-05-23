package com.example.springjunitpractice.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // checkpoint - 리팩토링 해야함!
    Optional<Account> findByNumber(int number);
    
}
