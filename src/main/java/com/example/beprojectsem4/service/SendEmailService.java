package com.example.beprojectsem4.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

public interface SendEmailService {
    void sendEmail(String email,String subject,String body) throws IOException, MessagingException;
}
