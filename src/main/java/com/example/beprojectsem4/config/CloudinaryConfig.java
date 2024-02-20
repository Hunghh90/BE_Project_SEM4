package com.example.beprojectsem4.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary("cloudinary://938378741863633:mmkBCCUhK8ysmwHC2tc9qbE8bP4@dj05iegz7");
    }

}
