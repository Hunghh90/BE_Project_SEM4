package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.UserAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttachmentRepository extends JpaRepository<UserAttachmentEntity,Long> {
    UserAttachmentEntity findUserAttachmentEntityByUser_UserId(Long id);
}
