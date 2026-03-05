package com.smartops.smartops.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Provides the currently authenticated username for JPA Auditing
 * (@CreatedBy and @LastModifiedBy fields in BaseEntity).
 *
 * Returns "SYSTEM" for unauthenticated operations (e.g., Flyway seed data,
 * scheduled jobs, or startup initialization).
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String SYSTEM_USER = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Return SYSTEM if no authentication exists or request is anonymous
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(SYSTEM_USER);
        }

        // Return the authenticated user's username (principal name)
        return Optional.ofNullable(authentication.getName())
                .filter(name -> !name.isBlank())
                .or(() -> Optional.of(SYSTEM_USER));
    }
}
