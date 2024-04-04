package com.example.beprojectsem4.repository;

import com.example.beprojectsem4.entities.PartnerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long>, JpaSpecificationExecutor<PartnerEntity> {
    PartnerEntity findByEmail(String email);
    PartnerEntity findByPartnerName(String partnerName);
    Page<PartnerEntity> findByPartnerNameContaining(String partnerName,@Nullable Pageable pageable);
    @Query("SELECT MONTH(p.createdAt) as month, COUNT(p) as count FROM PartnerEntity p WHERE YEAR(p.createdAt) = :year GROUP BY MONTH(p.createdAt)")
    List<Object[]> countPartnersByMonth(int year);
}
