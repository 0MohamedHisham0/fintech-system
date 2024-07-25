package com.system.fintech.service;

import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.model.Token;
import com.system.fintech.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public Token findById(long id) {
        return tokenRepository.findById(id);
    }

    public Token findValidToken(String token, UserTypeEnum userTypeEnum) {
        return tokenRepository.findValidToken(token, userTypeEnum.name());
    }

    void inValidToken(String token) {
        tokenRepository.inValidToken(token);
    }
    void inValidToken(long userId) {
        tokenRepository.inValidToken(userId);
    }

    public void createToken(Token token) {
        tokenRepository.save(token);
    }
}
