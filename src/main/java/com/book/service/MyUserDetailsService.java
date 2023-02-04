package com.book.service;

import com.book.model.UsersEntity;
import com.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsersEntity> usersEntity = userRepository.findUsersEntityByUserName(username);
        if (usersEntity.isPresent()) {
            return new User(usersEntity.get().getUserName(), usersEntity.get().getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User Not Found with -> username or email: " + username);
        }

    }
}
