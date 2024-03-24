package com.example.beprojectsem4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long>, JpaSpecificationExecutor<ProgramEntity> {
    Page<ProgramEntity> findByProgramNameContaining(String programrName, Pageable pageable);
    ProgramEntity findByUser(UserEntity userEntity);
}
