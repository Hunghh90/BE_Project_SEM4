package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface DonationService {
     ResponseEntity<?> DonationSuccess(HttpServletRequest request, CreateDonateDto donateDto);
}
