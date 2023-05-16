# Spring-Junit-Practice
- 스프링부트 JUint 테스트 실습 코드입니다.
- (참고) [인프런] 최주호님 스프링부트 JUnit 테스트 강의
<br><br>

### Jpa LocalDateTime 자동 생성하는 법
- @EnableJpaAuditing - Main 클래스
- @EntityListeners(AuditingEntityListener.class) - Entity 클래스
```java
@CreatedDate
@Column(nullable = false)
private LocalDateTime created;

@LastModifiedDate
@Column(nullable = false)
private LocalDateTime updated;
```
