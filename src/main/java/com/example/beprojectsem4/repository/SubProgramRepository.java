package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.SubProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SubProgramRepository extends JpaRepository<SubProgramEntity, Long> , JpaSpecificationExecutor<SubProgramEntity> {
    SubProgramEntity findByUserAndProgramAndType(UserEntity user, ProgramEntity program,String type);
    Page<SubProgramEntity> findAllByUser(UserEntity user, PageRequest pageRequest);
    Page<SubProgramEntity> findAllByProgramAndStatus(ProgramEntity program,String status,@Nullable PageRequest pageRequest);
//    List<SubProgramEntity> findAllByProgramAndStatus(ProgramEntity program, String status);
}
