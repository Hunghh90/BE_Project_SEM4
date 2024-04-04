package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long>, JpaSpecificationExecutor<ProgramEntity> {
    Page<ProgramEntity> findByProgramNameContaining(String programName, Pageable pageable);
    ProgramEntity findByProgramName(String programName);
    Page<ProgramEntity> findByStatus(String status, Pageable pageable);
    @Query("SELECT MONTH(p.createdAt) as month, COUNT(p) as count FROM ProgramEntity p WHERE YEAR(p.createdAt) = :year GROUP BY MONTH(p.createdAt)")
    List<Object[]> countProgramsByMonth(int year);
}
