package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long>, JpaSpecificationExecutor<ProgramEntity> {
}
