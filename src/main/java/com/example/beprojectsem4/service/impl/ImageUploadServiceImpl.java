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
    public String imageUpload(MultipartFile files) {
        try {

                Map uploadResult = cloudinary.uploader().upload(files.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                return imageUrl;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
