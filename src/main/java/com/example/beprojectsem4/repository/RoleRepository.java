package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleByRoleName(String roleName);
}
