package com.book.repository;

import com.book.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Integer> {
    Boolean existsByUserName(String username);
    Optional<UsersEntity> findUsersEntityByUserName(String username);
}
