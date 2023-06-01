package com.example.springjunitpractice.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springjunitpractice.config.dummy.DummyObject;
import com.example.springjunitpractice.domain.account.Account;
import com.example.springjunitpractice.domain.account.AccountRepository;
import com.example.springjunitpractice.domain.user.User;
import com.example.springjunitpractice.domain.user.UserRepository;

// 직접 생성한 Repository 클래스이기 때문에 테스트 필요함

@DataJpaTest // DB 관련 빈 등록
public class TransactionRepositoryImplTest extends DummyObject {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoincrementReset(); // 기본키 초기화
        dataSetting();
        em.clear(); // 정확한 테스트를 위해 영속성 컨텍스트 초기화
    }

    private void autoincrementReset() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
    
    @Test
    public void findTransactionList_all_test() throws Exception {
        // given
        Long accountId = 1L;
    
        // when
        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountId, "ALL", 0);
        transactionListPS.forEach((t) -> {
            System.out.println("테스트: " + t.getId());
            System.out.println("테스트: " + t.getGubun());
            System.out.println("테스트: " + t.getAmount());
            System.out.println("테스트: " + t.getSender());
            System.out.println("테스트: " + t.getReceiver());
            System.out.println("테스트: " + t.getDepositAccountBalance());
            System.out.println("테스트: " + t.getWithdrawAccountBalance());
            System.out.println("테스트: " + "===========================");
        });
    
        // then
        assertThat(transactionListPS.get(3).getDepositAccountBalance()).isEqualTo(800L);
    }

    private void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스,"));
        User love = userRepository.save(newUser("love", "러브"));
        User admin = userRepository.save(newUser("admin", "관리자"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

        transactionRepository.save(newWithdrawTransaction(ssarAccount1, accountRepository));
        transactionRepository.save(newDepositTransaction(cosAccount, accountRepository));
        transactionRepository.save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        transactionRepository.save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        transactionRepository.save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }
}
