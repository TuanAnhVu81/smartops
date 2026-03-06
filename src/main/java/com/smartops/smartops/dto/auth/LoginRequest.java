package com.smartops.smartops.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Login request DTO — immutable Java Record.
 * Uses @NotBlank for automatic validation when paired with @Valid on the controller.
 */
public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {}
