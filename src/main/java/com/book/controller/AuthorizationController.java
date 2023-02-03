package com.book.controller;

import com.book.model.JwtModel;
import com.book.model.UsersEntity;
import com.book.service.MyUserDetailsService;
import com.book.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final MyUserDetailsService myUserDetailsService;

    @PostMapping
    public ResponseEntity<JwtModel> getJwtToken(@RequestBody UsersEntity usersEntity) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usersEntity.getUserName(), usersEntity.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(usersEntity.getUserName());

        return ResponseEntity.ok(new JwtModel(jwtUtils.generateJwtToken(userDetails)));
    }
}
