package com.example.springjunitpractice.domain.account;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.handler.exception.CustomApiException;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_db")
@Entity
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private Long number; // 계좌번호

    @Column(nullable = false, length = 4)
    private int password; // 계좌 비밀번호

    @Column(nullable = false)
    private Long balance; // 잔액 (기본값 1000원)

    // FK는 Many 엔티티에 생성됨
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated;

    @Builder
    public Account(Long id, Long number, int password, Long balance, User user, LocalDateTime created,
            LocalDateTime updated) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.created = created;
        this.updated = updated;
    }

    public void checkOwner(Long userId) {
        // User 엔티티가 LAZY 로딩이어도 Account 테이블에서 user_id는 조회할 수 있기 때문에,
        // user.getId()를 호출해도 User 엔티티의 로딩이 일어나진 않음
        if (user.getId() != userId) {
            throw new CustomApiException("계좌의 소유자가 아닙니다.");
        }
    }

    public void deposit(Long amount) {
        balance += amount;
    }
}
