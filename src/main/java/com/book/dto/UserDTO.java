package com.book.dto;

import lombok.Data;

@Data
public class UserDTO extends JwtModel{
    private final String password;
}
