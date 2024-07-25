package com.system.fintech;

import com.system.fintech.dto.TokenUser;
import com.system.fintech.enums.TransactionTypeEnum;
import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.exception.CustomException;
import com.system.fintech.model.Transaction;
import com.system.fintech.model.User;
import com.system.fintech.repository.TransactionRepository;
import com.system.fintech.repository.UserRepository;
import com.system.fintech.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        com.system.fintech.repository.UserRepository.class,
        com.system.fintech.repository.TransactionRepository.class,
        com.system.fintech.service.TransactionService.class,
})
@EnableAutoConfiguration
public class TransactionServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        User user = User.builder()
                .name("John Doe")
                .username("john")
                .phoneNumber("1234567890")
                .password("password")
                .email("john@example.com")
                .balance(BigDecimal.valueOf(1000))
                .isActive(true)
                .build();
        userRepository.save(user);
    }

    @Test
    @Transactional
    public void testDeposit() {
        TokenUser admin = new TokenUser(1L, UserTypeEnum.ADMIN, new Date());
        Long userId = userRepository.findAll().get(0).getId();

        transactionService.deposit(userId, BigDecimal.valueOf(200), admin);

        User updatedUser = userRepository.findById(userId).orElseThrow();

        assertThat(updatedUser.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1200));
        Transaction transaction = transactionRepository.findAll().get(0);
        assertThat(transaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        assertThat(transaction.getType()).isEqualTo(TransactionTypeEnum.DEPOSIT);
    }
    @Test
    public void testWithdraw() {
        TokenUser admin = new TokenUser(1L, UserTypeEnum.ADMIN, new Date());
        Long userId = userRepository.findAll().get(0).getId();

        transactionService.withdraw(userId, BigDecimal.valueOf(200), admin);

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800));
        Transaction transaction = transactionRepository.findAll().get(0);
        assertThat(transaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        assertThat(transaction.getType()).isEqualTo(TransactionTypeEnum.WITHDRAW);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        TokenUser user = new TokenUser(1L, UserTypeEnum.USER, new Date());
        Long userId = userRepository.findAll().get(0).getId();

        CustomException thrown = assertThrows(CustomException.class, () -> transactionService.withdraw(userId, BigDecimal.valueOf(2000), user));

        assertThat(thrown.getMessage()).contains("Insufficient funds");
    }
}
