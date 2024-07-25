package com.system.fintech.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseErrorResponse {
    String message;
    String internalErrorCode;
}
