package com.smartops.smartops.dto.response;

import java.time.LocalDateTime;

/**
 * Common response DTO for User entities.
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        String fullName,
        String departmentName,
        String role,
        Boolean isActive,
        LocalDateTime createdAt
) {}
