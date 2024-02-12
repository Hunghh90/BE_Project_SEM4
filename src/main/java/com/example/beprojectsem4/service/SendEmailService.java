package com.example.beprojectsem4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

public interface SendEmailService {
    void sendEmail(String email,String subject,String body);
}
