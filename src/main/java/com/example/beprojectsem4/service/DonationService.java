package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dtos.Donation.DonateDto;
import jakarta.servlet.http.HttpServletRequest;

public interface DonationService {
     void DonationSuccess(HttpServletRequest request,DonateDto donateDto);
}
