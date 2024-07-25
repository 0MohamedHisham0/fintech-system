package com.system.fintech.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UpdateAdminRoleReq {
    Long roleId;
    List<String> permissions;
}
