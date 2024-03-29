package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dtos.donationDtos.CreateDonateDto;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DonationService {
     ResponseEntity<?> donationSuccess(HttpServletRequest request, CreateDonateDto donateDto);

    ResponseEntity<byte[]> generateDonationPDF(HttpServletRequest request, Long programId) throws  IOException;

    ResponseEntity<Object> makePayment(RequestDonate paymentRequest);

//     RedirectView payReturn(HttpServletRequest request);
//     ResponseEntity<?> listDonateByProgramName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
//     ResponseEntity<?> listDonateByUserName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
}
