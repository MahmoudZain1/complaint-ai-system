package com.ecommerce.complaints.exception;

import com.ecommerce.complaints.model.enums.ComplaintErrors;
import com.ecommerce.complaints.model.enums.UserErrors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static com.ecommerce.complaints.model.enums.ComplaintErrors.*;
import static com.ecommerce.complaints.model.enums.UserErrors.*;


@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(ComplaintErrors error, Object... args) {
        super(MessageFormat.format(error.getMessage(), args));
        this.errorCode = error.getCode();
        this.httpStatus = determineHttpStatus(error.getCode());
    }

    public BusinessException(UserErrors error, Object... args) {
        super(MessageFormat.format(error.getMessage(), args));
        this.errorCode = error.getCode();
        this.httpStatus = determineHttpStatus(error.getCode());
    }



    private HttpStatus determineHttpStatus(String code) {
        return switch (code) {
            case "COMPLAINT_NOT_FOUND", "USER_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "INVALID_CREDENTIALS", "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            case "USER_INACTIVE" -> HttpStatus.FORBIDDEN;
            case "USER_ALREADY_EXISTS", "DUPLICATE_COMPLAINT", "RESPONSE_ALREADY_EXISTS" -> HttpStatus.CONFLICT;
            case "TOKEN_EXPIRED", "INVALID_TOKEN" -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}