-- 모든 제약 조건 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 내용 전체 삭제
truncate transaction_db;
truncate account_db;
truncate user_db;

-- 제약 조건 활성화
SET FOREIGN_KEY_CHECKS = 1;