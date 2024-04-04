package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
    List<UserEntity> findAllByStatus(String status);
    List<UserEntity> findAllByRoles_RoleName(String roleName);
    @Query("SELECT MONTH(u.createdAt) as month, COUNT(u) as count FROM UserEntity u WHERE YEAR(u.createdAt) = :year GROUP BY MONTH(u.createdAt)")
    List<Object[]> countUsersByMonth(int year);
}
