package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PartnerController {
    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload_image")
    public List<String> imageUpload(@RequestParam("file") List<MultipartFile> files){
        return imageUploadService.imageUpload(files);
    }
}
