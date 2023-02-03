package com.book.controller;

import com.book.model.UsersEntity;
import com.book.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UsersEntity usersEntity) {
        if (userService.registerUser(usersEntity).isEmpty()) {
            return new ResponseEntity<>("User failed to register", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
