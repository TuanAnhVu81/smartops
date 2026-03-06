package com.smartops.smartops.exception;

import lombok.Getter;

/**
 * Single unified application exception for all business logic errors.
 *
 * Instead of creating many specific exception classes, this one exception
 * carries an ErrorCode that fully describes the error type, HTTP status,
 * and default message. The GlobalExceptionHandler reads the ErrorCode
 * to build the correct response.
 *
 * Usage:
 *   throw new AppException(ErrorCode.USER_NOT_FOUND);
 *   throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS, "Username 'john' is already taken");
 */
@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    /** Use default message from ErrorCode */
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /** Override with a more specific message (e.g., include the actual value) */
    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
