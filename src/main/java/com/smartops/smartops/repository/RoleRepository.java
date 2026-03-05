package com.smartops.smartops.repository;

import com.smartops.smartops.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name (e.g., "ROLE_EMPLOYEE").
     * Used during user registration to assign a default role.
     */
    Optional<Role> findByRoleName(String roleName);
}
