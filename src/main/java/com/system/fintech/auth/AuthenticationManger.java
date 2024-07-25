package com.system.fintech.auth;

import com.system.fintech.dto.TokenUser;
import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.utils.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class AuthenticationManger {
    public TokenUser getAuthenticatedUserData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();

            UserTypeEnum userType = UserTypeEnum.valueOf(jwt.getClaim(Constants.JwtClaims.USER_TYPE));
            Long userId = jwt.getClaim(Constants.JwtClaims.USER_ID);
            return TokenUser.builder().userId(userId).userType(userType).build();
        }

        return null;
    }

    public String getAuthenticatedToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        return null;
    }
}
