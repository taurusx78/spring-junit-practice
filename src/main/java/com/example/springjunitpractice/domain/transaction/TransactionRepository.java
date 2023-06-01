package com.example.springjunitpractice.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

// (참고) 인터페이스는 인터페이스만 상속 가능
public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionDao {
    
}
