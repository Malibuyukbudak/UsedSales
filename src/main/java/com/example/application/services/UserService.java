package com.example.application.services;

import com.example.application.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findUser(Long id);

    Long count();

    User login(String email, String password);

    void delete(User user);

    void save(User user);
}
