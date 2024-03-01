package com.example.beprojectsem4.dtos.userDtos;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    @Nullable
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date bod;
    @Nullable
    @Column(name = "phone_number")
    private String phoneNumber;
    @Nullable
    private String displayName;
}
