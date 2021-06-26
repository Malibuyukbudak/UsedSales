package com.example.application.repository;

import com.example.application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> , JpaRepository<User, Long> {
    List<User> findByEmailAndPassword(String email, String password);

}
