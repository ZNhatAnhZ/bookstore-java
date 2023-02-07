package com.book.controller;

import com.book.dto.UserDTO;
import com.book.model.JwtModel;
import com.book.model.UsersEntity;
import com.book.service.UserService;
import com.book.util.JwtUtils;
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
    private final JwtUtils jwtUtils;

    @PostMapping ("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO userDTO) {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setPassword(userDTO.getPassword());
        usersEntity.setUserName(jwtUtils.getUserNameFromJwtToken(userDTO.getJwt()));

        if (userService.updateUser(usersEntity).isEmpty()) {
            return new ResponseEntity<>("User failed to update", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    @PostMapping ("/getUser")
    public ResponseEntity<UsersEntity> getUser(@RequestBody JwtModel jwtModel) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtModel.getJwt());
        Optional<UsersEntity> usersEntity = userService.getUserByUserName(username);

        if (usersEntity.isPresent()) {
            return new ResponseEntity<>(usersEntity.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new UsersEntity(), HttpStatus.BAD_REQUEST);
    }
}
