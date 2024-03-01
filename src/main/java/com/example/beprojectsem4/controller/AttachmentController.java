package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    @Autowired
    private ImageUploadService imageUploadService;
    @PostMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile files){
        return imageUploadService.imageUpload(files);
    }
}