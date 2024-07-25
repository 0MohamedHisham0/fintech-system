package com.system.fintech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AdminDto {
    private Long id;
    private String name;
    private String username;
    private String phoneNumber;
    private String password;
    private String email;
    private Boolean isActive;
    private Long adminRoleId;
}
