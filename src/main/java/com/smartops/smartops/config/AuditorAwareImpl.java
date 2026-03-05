package com.smartops.smartops.config;

import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

/**
 * Provides the current user for @CreatedBy and @LastModifiedBy.
 * Currently returns a mock value "SYSTEM_USER".
 * Once Spring Security is set up, this will be updated to fetch from SecurityContext.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO: Update this in Step 3 when Spring Security is integrated
        return Optional.of("SYSTEM_USER");
    }
}
