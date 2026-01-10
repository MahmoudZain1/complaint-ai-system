package com.ecommerce.complaints.exception;

import com.ecommerce.complaints.model.generate.ErrorVTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.complaints.model.enums.UserErrors.INVALID_EMAIL;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorVTO> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorVTO error = ErrorVTO.builder()
                .error(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorVTO> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());

        boolean isEmailError = ex.getBindingResult()
                .getFieldErrors().stream()
                .anyMatch(error -> "customerEmail".equals(error.getField()));
        ErrorVTO error = ErrorVTO.builder()
        .error(isEmailError ? INVALID_EMAIL.name() : "VALIDATION_ERROR")
        .message("Validation failed")
        .timestamp(LocalDateTime.now())
        .path(request.getDescription(false).replace("uri=", ""))
                .details(validationErrors)
        .status(Long.valueOf(HttpStatus.BAD_REQUEST.value())).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVTO> handleGenericException(Exception ex, WebRequest request) {
        ErrorVTO error = ErrorVTO.builder()
                .error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .message("An unexpected error occurred")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .status(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .build();

        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }


}
