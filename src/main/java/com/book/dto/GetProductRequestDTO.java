package com.book.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetProductRequestDTO {
    private Integer page;
    private Integer size;
    private String sort;

    public GetProductRequestDTO(Integer page, Integer size, String sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
}
