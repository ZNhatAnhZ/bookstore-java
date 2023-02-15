package com.book.service;

import com.book.dto.UserDTO;
import com.book.model.UsersEntity;

import java.util.Optional;

public interface UserServiceInterface {
    Optional<UsersEntity> registerUser(UsersEntity usersEntity);
    Boolean changePassword(String authHeader, UserDTO userDTO);
    Optional<UsersEntity> getUserByJwtToken(String userName);
}
