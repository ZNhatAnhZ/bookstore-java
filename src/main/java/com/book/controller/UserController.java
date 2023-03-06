package com.book.controller;

import com.book.dto.UserDTO;
import com.book.model.UsersEntity;
import com.book.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping ("/changePassword")
    public ResponseEntity<String> changePassword(@RequestHeader(name = "Authorization") String authHeader, @RequestBody UserDTO userDTO) {
        if (Boolean.TRUE.equals(userService.changePassword(authHeader, userDTO))) {
            return new ResponseEntity<>("User changed password successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User failed to change password", HttpStatus.BAD_REQUEST);
    }

    @GetMapping ("/getUser")
    public ResponseEntity<UsersEntity> getUser(@RequestHeader(name = "Authorization") String authHeader) {
        Optional<UsersEntity> usersEntity = userService.getUserByJwtToken(authHeader.substring(7));

        if (usersEntity.isPresent()) {
            return new ResponseEntity<>(usersEntity.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new UsersEntity(), HttpStatus.BAD_REQUEST);
    }
}
