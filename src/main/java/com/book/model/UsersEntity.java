package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "users", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_name")
    private String userName;
    @Basic
    @Column(name = "user_type")
    private String userType;
    @Basic
    @Column(name = "password")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id && Objects.equals(userName, that.userName) && Objects.equals(userType, that.userType) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userType, password);
    }

    @Override
    public String toString() {
        return "id: " + id + "userName: " + userName + "userType: " + userType;
    }
}
