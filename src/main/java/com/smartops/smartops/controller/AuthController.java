package com.smartops.smartops.controller;

import com.smartops.smartops.common.ApiResponse;
import com.smartops.smartops.dto.auth.AuthResponse;
import com.smartops.smartops.dto.auth.LoginRequest;
import com.smartops.smartops.dto.auth.RegisterRequest;
import com.smartops.smartops.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and registration endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Authenticate with username and password, returns a JWT token on success.
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate and receive a JWT access token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * POST /api/auth/register
     * Register a new user with ROLE_EMPLOYEE. Returns a JWT token for immediate login.
     */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new account with the default Employee role")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
