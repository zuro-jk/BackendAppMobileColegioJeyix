package com.jeyix.school_jeyix.core.security.repository;

import com.jeyix.school_jeyix.core.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
