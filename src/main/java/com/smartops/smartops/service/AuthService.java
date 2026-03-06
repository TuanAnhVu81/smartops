package com.smartops.smartops.service;

import com.smartops.smartops.dto.auth.AuthResponse;
import com.smartops.smartops.dto.auth.LoginRequest;
import com.smartops.smartops.dto.auth.RegisterRequest;

public interface AuthService {

    /**
     * Authenticate a user with username and password.
     * Throws BadCredentialsException if credentials are invalid.
     *
     * @param request login credentials
     * @return JWT token and user info
     */
    AuthResponse login(LoginRequest request);

    /**
     * Register a new user account with the EMPLOYEE role by default.
     * Throws BadRequestException if username or email is already taken.
     *
     * @param request registration data
     * @return JWT token and user info for the newly created user
     */
    AuthResponse register(RegisterRequest request);
}
