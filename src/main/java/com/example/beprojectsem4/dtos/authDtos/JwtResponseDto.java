package com.example.beprojectsem4.dtos.authDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponseDto {
    private String email;
    private String token;
    private Long expired;
    private String tokeType = "Bearer";

    public JwtResponseDto(String email , String token,Long expired) {
        this.email = email;
        this.token = token;
        this.expired = expired;
    }
}
