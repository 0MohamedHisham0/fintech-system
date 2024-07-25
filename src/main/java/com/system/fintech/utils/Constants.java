package com.system.fintech.utils;

public interface Constants {
    String SECRET_KEY_ALGORITHM = "HS256";

    interface JwtClaims {
        String USER_TYPE = "userType";
        String USER_ID = "userId";
        String ADMIN_ROLE_ID = "adminRoleId";
        String EMAIL = "email";
        String USER_NAME = "userName";
        String NAME = "name";
    }

    interface InternalErrorCodes {
        String THIS_DATA_IS_ALREADY_EXISTS = "100";
        String DB_VALIDATION = "101";
        String INVALID_USERNAME_OR_PASSWORD = "102";
        String INVALID_DATA = "103";
        String INVALID_PERMISSION = "104";
        String INSUFFICIENT_FUNDS = "105";
    }

    interface OpenApi {
        String AUTHORIZATION = "Authorization";
        String BEARER = "bearer";
        String JWT = "JWT";
    }
}