package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    Page<DonationEntity> findByProgramProgramNameContaining(String programName, Pageable pageable);
    Page<DonationEntity> findByUserDisplayNameContaining(String displayName, Pageable pageable);
}
