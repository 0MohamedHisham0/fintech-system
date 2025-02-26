package com.system.fintech.controller;

import com.system.fintech.auth.AuthenticationManger;
import com.system.fintech.dto.TokenUser;
import com.system.fintech.model.Transaction;
import com.system.fintech.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/admin/transaction")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthenticationManger authenticationManger;

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('user:depositFund')")
    public ResponseEntity<Transaction> deposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        TokenUser user = authenticationManger.getAuthenticatedUserData();
        Transaction transaction = transactionService.deposit(userId, amount, user);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAuthority('user:withdrawFund')")
    public ResponseEntity<Transaction> withdraw(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        TokenUser user = authenticationManger.getAuthenticatedUserData();
        Transaction transaction = transactionService.withdraw(userId, amount, user);
        return ResponseEntity.ok(transaction);
    }

}
