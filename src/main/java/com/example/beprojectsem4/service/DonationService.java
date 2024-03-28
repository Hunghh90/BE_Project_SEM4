package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dtos.DonationDtos.CreateDonateDto;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DonationService {
     ResponseEntity<?> donationSuccess(HttpServletRequest request, CreateDonateDto donateDto);
     ResponseEntity<Object> makePayment(RequestDonate paymentRequest);

//     RedirectView payReturn(HttpServletRequest request);
     ResponseEntity<byte[]> generateDonationPDF(Long programId) throws DocumentException, FileNotFoundException, IOException;
//     ResponseEntity<?> listDonateByProgramName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
//     ResponseEntity<?> listDonateByUserName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
}
