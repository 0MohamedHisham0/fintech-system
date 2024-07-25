package com.system.fintech.controller;


import com.system.fintech.dto.UserDto;
import com.system.fintech.dto.request.LoginRequest;
import com.system.fintech.dto.response.TokenResponse;
import com.system.fintech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = userService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/signUp")
    public ResponseEntity<TokenResponse> signUp(
            @RequestBody UserDto user
    ) {
        TokenResponse TokenResponse = userService.signUp(user);
        return ResponseEntity.ok(TokenResponse);
    }
    @PostMapping("/logout")
    public ResponseEntity<TokenResponse> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> create(
            @RequestBody UserDto user
    ) {
        userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().body("Deleted Successfully");
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto) {
        boolean updated = userService.update(userDto);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> checkBalance() {
        BigDecimal balance = userService.checkBalance();
        return ResponseEntity.ok(balance);
    }

}
