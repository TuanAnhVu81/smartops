package com.smartops.smartops.repository;

import com.smartops.smartops.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their unique username.
     * Used by CustomUserDetailsService for authentication.
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a username is already taken.
     * Used during user registration to prevent duplicates.
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email address is already registered.
     * Used during user registration to prevent duplicates.
     */
    boolean existsByEmail(String email);
}
