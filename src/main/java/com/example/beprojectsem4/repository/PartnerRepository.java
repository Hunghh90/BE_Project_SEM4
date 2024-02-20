package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    PartnerEntity findByEmail(String email);
    PartnerEntity findByPartnerName(String partnerName);
}
