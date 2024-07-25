package com.system.fintech.controller;

import com.system.fintech.auth.AuthenticationManger;
import com.system.fintech.dto.TokenUser;
import com.system.fintech.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/user/transaction")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserTransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AuthenticationManger authenticationManger;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam BigDecimal amount) {
        TokenUser user = authenticationManger.getAuthenticatedUserData();
        transactionService.deposit(user.getUserId(), amount, user);
        return ResponseEntity.ok("Funds deposited successfully.");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam BigDecimal amount) {
        TokenUser user = authenticationManger.getAuthenticatedUserData();
        transactionService.withdraw(user.getUserId(), amount,user);
        return ResponseEntity.ok("Funds withdrawn successfully.");
    }

}
