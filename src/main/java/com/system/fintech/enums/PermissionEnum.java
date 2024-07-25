package com.system.fintech.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnum {
    DEPOSIT_FUND("user:depositFund"),
    WITHDRAW_FUND("user:withdrawFund");

    private final String permission;
}
