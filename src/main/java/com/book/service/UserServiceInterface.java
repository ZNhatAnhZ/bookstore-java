package com.book.service;

import com.book.model.UsersEntity;

import java.util.Optional;

public interface UserServiceInterface {
    public Optional<UsersEntity> registerUser(UsersEntity usersEntity);
    public Optional<UsersEntity> updateUser(UsersEntity usersEntity);
    public Optional<UsersEntity> getUserByUserName(String userName);
}
