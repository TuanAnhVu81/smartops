package com.smartops.smartops.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * Standardized API response wrapper for all endpoints.
 *
 * Success response shape:
 *   { "success": true, "data": { ... } }
 *
 * Error response shape:
 *   { "success": false, "code": 2001, "message": "User not found" }
 *
 * @param <T> the type of the response data payload
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Omits null fields from JSON
public class ApiResponse<T> {

    private final boolean success;

    /** Stable numeric error code from ErrorCode enum — null on success */
    private final Integer code;

    /** Human-readable message — null on success (data speaks for itself) */
    private final String message;

    /** Response payload — null on error */
    private final T data;

    private ApiResponse(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 2xx — successful operation with a data payload */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, null, data);
    }

    /** 2xx — successful operation with a message and optional data */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, null, message, data);
    }

    /** 4xx/5xx — failed operation using ErrorCode (provides code + message) */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
