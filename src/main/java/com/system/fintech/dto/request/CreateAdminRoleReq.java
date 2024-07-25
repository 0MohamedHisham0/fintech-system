package com.system.fintech.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateAdminRoleReq {
    String roleName;
    List<String> permissions;
}
