package com.system.fintech.exception;


import com.sun.jdi.request.InvalidRequestStateException;
import com.system.fintech.dto.response.BaseErrorResponse;
import com.system.fintech.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseErrorResponse exception(NoHandlerFoundException exception) {
        log.error(exception.getMessage());

        return BaseErrorResponse.builder()
                .message("API Not Found")
                .internalErrorCode(HttpStatus.NOT_FOUND.value() + "")
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleValidationErrors(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());

        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        String firstError = errors.isEmpty() ? "No validation errors" : errors.get(0);

        return BaseErrorResponse.builder()
                .internalErrorCode(firstError)
                .build();
    }

    @ExceptionHandler(InvalidRequestStateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseErrorResponse invalidRequestException(InvalidRequestStateException exception, WebRequest request) {
        log.error(exception.getMessage());

        return BaseErrorResponse.builder()
                .internalErrorCode(HttpStatus.BAD_REQUEST.value() + "")
                .build();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseErrorResponse businessExceptionException(CustomException exception, WebRequest request) {
        log.error(exception.getMessage());

        return BaseErrorResponse.builder()
                .internalErrorCode(exception.getInternalErrorCode())
                .message(exception.getMessage())
                .build();
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public BaseErrorResponse forbiddenExceptionException(AuthorizationDeniedException exception) {
        log.error(exception.getMessage());

        return BaseErrorResponse.builder()
                .internalErrorCode(HttpStatus.FORBIDDEN.value() + "")
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseErrorResponse dataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        log.error(exception.getMessage());
        if (isUniqueConstraintViolation(exception)) {
            return BaseErrorResponse.builder()
                    .message("Duplicate Data in Database")
                    .internalErrorCode(Constants.InternalErrorCodes.THIS_DATA_IS_ALREADY_EXISTS)
                    .build();
        } else {
            return BaseErrorResponse.builder()
                    .internalErrorCode(Constants.InternalErrorCodes.DB_VALIDATION)
                    .build();
        }
    }

    private boolean isUniqueConstraintViolation(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        assert rootCause != null;
        return rootCause.getMessage().toLowerCase().contains("unique constraint");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseErrorResponse internServerErrorException(Exception exception, WebRequest request) {
        log.error(exception.getMessage());
        log.error(exception + " ");
        return BaseErrorResponse.builder()
                .internalErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + "")
                .build();
    }


}