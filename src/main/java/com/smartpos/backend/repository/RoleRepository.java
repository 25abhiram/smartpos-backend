package com.smartpos.backend.repository;

import com.smartpos.backend.entity.ERole;
import com.smartpos.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role>  findByName(ERole name);
}
