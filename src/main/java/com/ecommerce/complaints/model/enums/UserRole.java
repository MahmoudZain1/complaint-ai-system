package com.ecommerce.complaints.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    CUSTOMER("CUSTOMER", "Customer User"),
    EMPLOYEE("EMPLOYEE", "Employee User"),
    MANAGER("MANAGER", "Manager User"),
    ADMIN("ADMIN", "Administrator");

    private final String code;
    private final String name;

    public String getAuthority() {
        return "ROLE_" + code;
    }
}
