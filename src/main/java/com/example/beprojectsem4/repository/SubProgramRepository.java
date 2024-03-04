package com.example.beprojectsem4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.SubProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;

public interface SubProgramRepository extends JpaRepository<SubProgramEntity, Long> {
	  Page<SubProgramEntity> findBytypeContaining(String type,@Nullable Pageable pageable);
	  boolean existsByUserAndProgram(UserEntity user, ProgramEntity program);
}