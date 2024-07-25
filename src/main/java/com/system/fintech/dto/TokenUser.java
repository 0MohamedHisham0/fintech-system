package com.system.fintech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.system.fintech.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenUser {
    private Long userId;
    private UserTypeEnum userType;
    private Date createdAt;
}
