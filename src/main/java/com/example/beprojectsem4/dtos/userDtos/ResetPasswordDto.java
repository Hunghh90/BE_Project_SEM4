package com.example.beprojectsem4.dtos.userDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    private String email;
    private String newPassword;
    private String confirmPassword;
}
