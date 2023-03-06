package com.book.controller;

import com.book.dto.JwtModel;
import com.book.model.UsersEntity;
import com.book.security.MyUserDetailsService;
import com.book.security.UserDetailsImpl;
import com.book.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AuthorizationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<JwtModel> getJwtToken(@RequestBody UsersEntity usersEntity) throws BadCredentialsException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usersEntity.getUserName(), usersEntity.getPassword()));

            UserDetailsImpl userDetails =  (UserDetailsImpl) authentication.getPrincipal();
            return new ResponseEntity<>(new JwtModel(jwtUtils.generateJwtToken(userDetails)), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            log.error("Incorrect username or password", e);
        }
        return new ResponseEntity<>(new JwtModel(), HttpStatus.BAD_REQUEST);
    }
}
