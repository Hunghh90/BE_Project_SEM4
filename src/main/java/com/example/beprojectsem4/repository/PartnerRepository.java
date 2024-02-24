package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long>, JpaSpecificationExecutor<PartnerEntity> {
    PartnerEntity findByEmail(String email);
    PartnerEntity findByPartnerName(String partnerName);

}
