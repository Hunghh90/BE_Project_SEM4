package com.example.beprojectsem4.service;

import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.UserEntity;

public interface DonationService {
     void DonationSuccess(Long id, double amount,String paymentMethod);
     DonationEntity FindByUser(UserEntity entity);
}
