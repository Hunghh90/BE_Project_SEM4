package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.DonationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
}
