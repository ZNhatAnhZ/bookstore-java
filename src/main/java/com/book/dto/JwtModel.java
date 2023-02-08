package com.book.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class JwtModel {
    private String jwt;

    public JwtModel(String jwt) {
        this.jwt = jwt;
    }
}
