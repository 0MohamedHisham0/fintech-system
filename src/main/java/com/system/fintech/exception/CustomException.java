package com.system.fintech.exception;

import jakarta.persistence.PersistenceException;
import lombok.Getter;

@Getter
public class CustomException extends PersistenceException {
    private final String internalErrorCode;

    public CustomException(String message, String internalErrorCode) {
        super(message);
        this.internalErrorCode = internalErrorCode;
    }

    public CustomException(String internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }
}
