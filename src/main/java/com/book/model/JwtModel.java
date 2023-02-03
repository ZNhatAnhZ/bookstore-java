package com.book.model;

import lombok.*;

@Data
public class JwtModel {
    private String jwt;

    public JwtModel(String jwt) {
        this.jwt = jwt;
    }
}
