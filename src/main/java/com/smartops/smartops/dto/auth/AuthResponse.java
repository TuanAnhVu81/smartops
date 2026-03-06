package com.smartops.smartops.dto.auth;

/**
 * Authentication response DTO returned after successful login or registration.
 * Contains the JWT token and basic user info for the frontend to store.
 */
public record AuthResponse(
        String token,
        String username,
        String fullName,
        String role
) {}
