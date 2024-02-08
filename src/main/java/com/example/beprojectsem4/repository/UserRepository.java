package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);

    List<UserEntity> findAllByRoles_RoleName(String roleName);
}
