package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dto.response.ResponseDonate;
import com.example.beprojectsem4.service.impl.VNPayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Validated
@RequestMapping("/api/v1")
public class DonationController {
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/donation")
    public ResponseEntity<String> makePayment(@Valid @RequestBody RequestDonate paymentRequest) {
        String paymentMethod = paymentRequest.getPaymentMethod();
        // Xử lý logic thanh toán ở đây

        if ("VNPay".equals(paymentMethod)) {
            // Sử dụng VNPayService để xử lý thanh toán VNPay
            return ResponseEntity.ok(vnPayService.createOrder(paymentRequest));
        } else if ("Paypal".equals(paymentMethod)) {
            // Chuyển hướng đến PaypalController
            return null;
//            return paypalController.processPayment(paymentRequest);
        } else {
            return ResponseEntity.badRequest().body("Invalid payment method");
        }
    }
}
