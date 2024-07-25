package com.system.fintech.service;

import com.system.fintech.dto.TokenUser;
import com.system.fintech.enums.TransactionTypeEnum;
import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.exception.CustomException;
import com.system.fintech.model.Transaction;
import com.system.fintech.model.User;
import com.system.fintech.repository.TransactionRepository;
import com.system.fintech.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.system.fintech.utils.Constants.InternalErrorCodes.INSUFFICIENT_FUNDS;

@Service
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void deposit(Long userId, BigDecimal amount, TokenUser currentUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(amount)
                .type(TransactionTypeEnum.DEPOSIT)
                .build();

        if (currentUser.getUserType() == UserTypeEnum.ADMIN) transaction.setAdminId(currentUser.getUserId());

        transactionRepository.save(transaction);

        BigDecimal newBalance = user.getBalance().add(amount);
        userRepository.update(userId, newBalance, user.getBalance());
    }


    @Transactional
    public void withdraw(Long userId, BigDecimal amount, TokenUser currentUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance().compareTo(amount) < 0) {
            throw new CustomException("Insufficient funds", INSUFFICIENT_FUNDS);
        }

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(amount)
                .type(TransactionTypeEnum.WITHDRAW)
                .build();

        if (currentUser.getUserType() == UserTypeEnum.ADMIN) transaction.setAdminId(currentUser.getUserId());

        transactionRepository.save(transaction);

        BigDecimal newBalance = user.getBalance().subtract(amount);
        userRepository.update(userId, newBalance, user.getBalance());
    }


}
