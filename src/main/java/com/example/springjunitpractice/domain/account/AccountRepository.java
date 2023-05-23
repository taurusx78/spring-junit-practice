package com.example.springjunitpractice.domain.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // checkpoint - 리팩토링 해야함!
    Optional<Account> findByNumber(Long number);
    
    // select * from account where user_id = :userId
    List<Account> findByUser_id(Long userId);
}
