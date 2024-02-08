package com.example.beprojectsem4.dtos.roleDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleDto {
    private String oldRoleName;
    private String newRoleName;
}
