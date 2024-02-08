package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.roleDtos.UpdateRoleDto;
import com.example.beprojectsem4.entities.RoleEntity;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    RoleEntity findRoleByRoleName(String roleName);
    ResponseEntity<?> createRole(String roleName);

    ResponseEntity<?> getRoles();

    ResponseEntity<?> updateRole(UpdateRoleDto updateRoleDto);

    ResponseEntity<?> deleteRole(String roleName);
}
