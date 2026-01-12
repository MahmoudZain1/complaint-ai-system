package com.ecommerce.complaints.model.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrors {

    USER_NOT_FOUND("USER_NOT_FOUND", "User not found with email: {0}"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User already exists with email: {0}"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid email or password"),
    USER_INACTIVE("USER_INACTIVE", "User account is inactive: {0}"),
    INVALID_INPUT("INVALID_INPUT", "Invalid input: {0}"),
    MISSING_REQUIRED_FIELD("MISSING_REQUIRED_FIELD", "Missing required field: {0}"),
    INVALID_EMAIL("INVALID_EMAIL", "Invalid email format: {0}"),
    PASSWORD_TOO_SHORT("PASSWORD_TOO_SHORT", "Password must be at least 6 characters long"),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized access"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "JWT token has expired"),
    INVALID_TOKEN("INVALID_TOKEN", "Invalid JWT token");

    private final String code;
    private final String message;
}
