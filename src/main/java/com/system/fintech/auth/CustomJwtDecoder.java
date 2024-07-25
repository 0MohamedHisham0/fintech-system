package com.system.fintech.auth;

import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.model.Admin;
import com.system.fintech.model.Token;
import com.system.fintech.model.User;
import com.system.fintech.service.AdminService;
import com.system.fintech.service.TokenService;
import com.system.fintech.service.UserService;
import com.system.fintech.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {
    private final JwtDecoder jwtDecoder;
    private final String secretKey;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    public CustomJwtDecoder(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;

        SecretKey key = new SecretKeySpec(secretKey.getBytes(), Constants.SECRET_KEY_ALGORITHM);
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt decodedJwt;
        try {
            decodedJwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.error("Failed to decode JWT: {}", token, e);
            throw e;
        }

        if (!validateJwt(decodedJwt)) {
            log.error("Invalid Token: {}", decodedJwt.getTokenValue());
            throw new JwtException("Invalid Token");
        }

        return decodedJwt;
    }

    private boolean validateJwt(Jwt decodedJwt) {
        try {
            String userRoleStr = decodedJwt.getClaim(Constants.JwtClaims.USER_TYPE);
            Long userId = decodedJwt.getClaim(Constants.JwtClaims.USER_ID);

            if (userRoleStr == null || userId == null) {
                log.error("Missing required claims in JWT");
                return false;
            }

            UserTypeEnum userType = UserTypeEnum.valueOf(userRoleStr);
            switch (userType) {
                case USER -> {
                    User user = userService.getUser(userId);
                    if (user == null) {
                        log.error("User not found for role: {} and ID: {}", userType, userId);
                        return false;
                    }
                }
                case ADMIN -> {
                    Admin admin = adminService.getUser(userId);
                    if (admin == null) {
                        log.error("User not found for role: {} and ID: {}", userType, userId);
                        return false;
                    }
                }
            }

            Token token = tokenService.findValidToken(decodedJwt.getTokenValue(), userType);
            if (token == null) {
                log.error("Token not found or invalid: {}", decodedJwt.getTokenValue());
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("JWT validation failed", e);
            return false;
        }
    }

}
