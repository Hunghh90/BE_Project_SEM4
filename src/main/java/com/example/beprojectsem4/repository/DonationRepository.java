package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
	DonationEntity findByUser(UserEntity entity);
}
