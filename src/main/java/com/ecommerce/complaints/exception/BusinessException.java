package com.ecommerce.complaints.exception;

import com.ecommerce.complaints.model.enums.ComplaintErrors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;


@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(ComplaintErrors error, Object... args) {
        super(MessageFormat.format(error.getMessage(), args));
        this.errorCode = error.getCode();
        this.httpStatus = determineHttpStatus(error);

    }

    public BusinessException(ComplaintErrors error, HttpStatus httpStatus) {
        super(error.getMessage());
        this.errorCode = error.getCode();
        this.httpStatus = httpStatus;
    }


    private HttpStatus determineHttpStatus(ComplaintErrors error) {
        return switch (error) {
            case COMPLAINT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case RESPONSE_ALREADY_EXISTS, DUPLICATE_COMPLAINT -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}