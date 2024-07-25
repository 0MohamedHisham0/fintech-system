package com.system.fintech.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.system.fintech.dto.TokenUser;
import com.system.fintech.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(TokenUser tokenUser) {
        return JWT.create()
                .withClaim(Constants.JwtClaims.EMAIL, tokenUser.getEmail())
                .withClaim(Constants.JwtClaims.NAME, tokenUser.getName())
                .withClaim(Constants.JwtClaims.USER_ID, tokenUser.getUserId())
                .withClaim(Constants.JwtClaims.USER_TYPE, tokenUser.getUserType().name())
                .withClaim(Constants.JwtClaims.USER_NAME, tokenUser.getUsername())
                .withClaim(Constants.JwtClaims.ADMIN_ROLE_ID, tokenUser.getAdminRoleId())
                .withIssuedAt(new Date(System.currentTimeMillis()).toInstant())
                .sign(Algorithm.HMAC256(secretKey));
    }
}
