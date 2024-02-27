package com.example.beprojectsem4.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    String imageUpload(MultipartFile file);
}
