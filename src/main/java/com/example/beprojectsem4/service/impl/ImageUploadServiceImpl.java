package com.example.beprojectsem4.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.beprojectsem4.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<String> imageUpload(List<MultipartFile> files) {
        try {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : files) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String url = uploadResult.get("url").toString();
                urls.add(url);
            }
            return urls;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
