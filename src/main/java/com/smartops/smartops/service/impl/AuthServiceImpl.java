package com.smartops.smartops.service.impl;

import com.smartops.smartops.dto.auth.AuthResponse;
import com.smartops.smartops.dto.auth.LoginRequest;
import com.smartops.smartops.dto.auth.RegisterRequest;
import com.smartops.smartops.entity.Role;
import com.smartops.smartops.entity.User;
import com.smartops.smartops.exception.AppException;
import com.smartops.smartops.exception.ErrorCode;
import com.smartops.smartops.repository.RoleRepository;
import com.smartops.smartops.repository.UserRepository;
import com.smartops.smartops.security.JwtTokenProvider;
import com.smartops.smartops.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "ROLE_EMPLOYEE";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticate with username and password.
     * Delegates credential verification to Spring Security's AuthenticationManager.
     * BadCredentialsException is caught by GlobalExceptionHandler → 401 INVALID_CREDENTIALS.
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Trigger Spring Security authentication — throws BadCredentialsException if wrong
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // Cast principal to our User entity (returned by CustomUserDetailsService)
        User user = (User) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);

        log.info("User '{}' logged in successfully", user.getUsername());
        return buildAuthResponse(token, user);
    }

    /**
     * Register a new user account with the default ROLE_EMPLOYEE role.
     * Throws AppException with specific ErrorCode on duplicate username or email.
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check username uniqueness
        if (userRepository.existsByUsername(request.username())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS,
                    "Username '" + request.username() + "' is already taken");
        }

        // Check email uniqueness
        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email '" + request.email() + "' is already registered");
        }

        // Lookup the default role from DB (must exist via V2 seed migration)
        Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND,
                        "Default role '" + DEFAULT_ROLE + "' not found. Please run DB seed migration."));

        // Build and persist new user with encoded password
        User newUser = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .fullName(request.fullName())
                .role(defaultRole)
                .isActive(true)
                .build();

        userRepository.save(newUser);

        // Generate JWT for immediate session after registration
        String token = jwtTokenProvider.generateToken(newUser);

        log.info("New user '{}' registered successfully", newUser.getUsername());
        return buildAuthResponse(token, newUser);
    }

    // ── Private Helper ────────────────────────────────────────────────────

    private AuthResponse buildAuthResponse(String token, User user) {
        return new AuthResponse(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getRole().getRoleName()
        );
    }
}
