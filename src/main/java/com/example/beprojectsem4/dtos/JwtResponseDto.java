package com.example.beprojectsem4.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponseDto {
    private String email;
    private String token;
    private String tokeType = "Bearer";

    public JwtResponseDto(String email , String token) {
        this.email = email;
        this.token = token;
    }
}
