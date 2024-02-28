package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramAttachmentRepository extends JpaRepository<ProgramAttachmentEntity, Long> {
    List<ProgramAttachmentEntity> findByProgramId_programId(Long programId);
}
