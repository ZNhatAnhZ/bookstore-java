package com.book.service;

import com.book.model.UsersEntity;
import com.book.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<?> registerUser(UsersEntity usersEntity) {
        usersEntity.setUserType("user");

        if(userRepository.existsByUserName(usersEntity.getUserName())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        String encryptedPassword = passwordEncoder.encode(usersEntity.getPassword());
        usersEntity.setPassword(encryptedPassword);
        userRepository.save(usersEntity);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }
}
