package com.example.springjunitpractice.domain.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 계좌를 조회할 때 User 엔티티가 자주 필요하다면 JOIN FETCH로 함께 조회하기!
    // @Query("SELECT ac FROM Account ac JOIN FETCH ac.user WHERE ac.number = :number)
    Optional<Account> findByNumber(@Param("number") Long number);
    
    // select * from account where user_id = :userId
    List<Account> findByUser_id(Long userId);
}
