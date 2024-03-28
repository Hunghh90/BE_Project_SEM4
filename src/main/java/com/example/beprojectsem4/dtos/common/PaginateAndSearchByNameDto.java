package com.example.beprojectsem4.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class PaginateAndSearchByNameDto {
    @Nullable
    private String name;
    @Nullable
    private Integer page;
    @Nullable
    private Integer size;

    public PaginateAndSearchByNameDto(Integer page, Integer size) {
        this.page = (page != null && page > 0) ? page : 1;
        this.size = (size != null && size > 0) ? size : 10;
    }
}
