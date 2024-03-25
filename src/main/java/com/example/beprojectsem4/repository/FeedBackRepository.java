package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.FeedBackEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface FeedBackRepository extends JpaRepository<FeedBackEntity, Long> {
    Page<FeedBackEntity> findAllByProgram(ProgramEntity program,@Nullable Pageable pageable);
}
