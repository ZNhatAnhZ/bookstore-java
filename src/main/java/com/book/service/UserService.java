package com.book.service;

import com.book.model.UsersEntity;
import com.book.repository.UserRepository;
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

    @Override
    @Transactional
    public Optional<UsersEntity> registerUser(UsersEntity usersEntity) {
        usersEntity.setUserType("user");
        Optional<UsersEntity> usersEntityOptional = Optional.empty();

        if(!userRepository.existsByUserName(usersEntity.getUserName())){
            String encryptedPassword = passwordEncoder.encode(usersEntity.getPassword());
            usersEntity.setPassword(encryptedPassword);
            usersEntityOptional = Optional.of(userRepository.save(usersEntity));
        }

        return usersEntityOptional;

    }
}
