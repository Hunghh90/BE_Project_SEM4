package com.example.beprojectsem4.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
    String oldPassword;
    String newPassword;
    String confirmPassword;
}
