package com.example.beprojectsem4.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginateAndSearchByNameDto {
    @Nullable
    private String name;
    @Nullable
    private int page;
    @Nullable
    private int size;
}
