package com.book.security;

import com.book.model.UsersEntity;
import com.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsersEntity> usersEntity = userRepository.findUsersEntityByUserName(username);
        if (usersEntity.isPresent()) {
            return new UserDetailsImpl(usersEntity.get());
        } else {
            throw new UsernameNotFoundException("User Not Found with -> username or email: " + username);
        }

    }
}
