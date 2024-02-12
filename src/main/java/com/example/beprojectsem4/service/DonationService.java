package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dto.request.RequestDonate;

public interface DonationService {
     void DonationSuccess(Long id, double amount,String paymentMethod);
}
