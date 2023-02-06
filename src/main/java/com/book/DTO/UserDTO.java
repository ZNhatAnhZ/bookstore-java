package com.book.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private final String jwt;
    private final String password;
}
