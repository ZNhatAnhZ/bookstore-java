package com.book.service;

import com.book.model.UsersEntity;
import com.book.repository.UserRepository;
import com.book.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserServiceInterface{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Optional<UsersEntity> registerUser(UsersEntity usersEntity) {
        usersEntity.setUserType("user");
        Optional<UsersEntity> usersEntityOptional = Optional.empty();

        if(Boolean.FALSE.equals(userRepository.existsByUserName(usersEntity.getUserName()))){
            String encryptedPassword = passwordEncoder.encode(usersEntity.getPassword());
            usersEntity.setPassword(encryptedPassword);
            usersEntityOptional = Optional.of(userRepository.save(usersEntity));
        }

        return usersEntityOptional;

    }

    @Override
    @Transactional
    public Optional<UsersEntity> updateUser(UsersEntity usersEntity) {
        Optional<UsersEntity> usersEntityOptional = userRepository.findUsersEntityByUserName(usersEntity.getUserName());
        Optional<UsersEntity> result = Optional.empty();

        if(usersEntityOptional.isPresent()){
            String encryptedPassword = passwordEncoder.encode(usersEntity.getPassword());
            usersEntityOptional.get().setPassword(encryptedPassword);
            result = Optional.of(userRepository.save(usersEntityOptional.get()));
        }

        return result;
    }

    @Override
    @Transactional
    public Optional<UsersEntity> getUserByUserName(String userName) {
        return userRepository.findUsersEntityByUserName(userName);
    }

    @Override
    @Transactional
    public Optional<UsersEntity> getUserByJwtToken(String jwt) {
        return userRepository.findUsersEntityByUserName(jwtUtils.getUserNameFromJwtToken(jwt));
    }
}
