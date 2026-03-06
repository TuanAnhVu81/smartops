package com.smartops.smartops.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Centralized error code registry for the entire SmartOps system.
 *
 * Each enum constant defines:
 *  - code:       a fixed integer, communicated to frontend for i18n or conditional logic
 *  - httpStatus: the HTTP status code to return
 *  - message:    a default human-readable message (can be overridden in AppException)
 *
 * Convention:
 *  1xxx  → Authentication / Authorization errors
 *  2xxx  → User-related errors
 *  3xxx  → Ticket-related errors
 *  4xxx  → Department / Role errors
 *  9xxx  → System / Validation errors
 */
@Getter
public enum ErrorCode {

    // ── 1xxx: Auth & Security ─────────────────────────────────────────────
    INVALID_CREDENTIALS         (1001, HttpStatus.UNAUTHORIZED,  "Invalid username or password"),
    TOKEN_INVALID               (1002, HttpStatus.UNAUTHORIZED,  "Token is invalid or expired"),
    ACCESS_DENIED               (1003, HttpStatus.FORBIDDEN,     "You do not have permission to perform this action"),
    ACCOUNT_DISABLED            (1004, HttpStatus.FORBIDDEN,     "Your account has been disabled"),

    // ── 2xxx: User ────────────────────────────────────────────────────────
    USER_NOT_FOUND              (2001, HttpStatus.NOT_FOUND,     "User not found"),
    USERNAME_ALREADY_EXISTS     (2002, HttpStatus.BAD_REQUEST,   "Username is already taken"),
    EMAIL_ALREADY_EXISTS        (2003, HttpStatus.BAD_REQUEST,   "Email is already registered"),

    // ── 3xxx: Ticket ──────────────────────────────────────────────────────
    TICKET_NOT_FOUND            (3001, HttpStatus.NOT_FOUND,     "Ticket not found"),
    TICKET_ALREADY_RESOLVED     (3002, HttpStatus.BAD_REQUEST,   "Ticket has already been resolved"),
    TICKET_ACTION_NOT_PERMITTED (3003, HttpStatus.FORBIDDEN,     "You are not allowed to perform this action on the ticket"),

    // ── 4xxx: Department / Role ───────────────────────────────────────────
    DEPARTMENT_NOT_FOUND        (4001, HttpStatus.NOT_FOUND,     "Department not found"),
    ROLE_NOT_FOUND              (4002, HttpStatus.NOT_FOUND,     "Role not found"),

    // ── 9xxx: System / Validation ─────────────────────────────────────────
    VALIDATION_FAILED           (9001, HttpStatus.BAD_REQUEST,   "Request validation failed"),
    RESOURCE_NOT_FOUND          (9002, HttpStatus.NOT_FOUND,     "Requested resource not found"),
    INTERNAL_SERVER_ERROR       (9999, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
