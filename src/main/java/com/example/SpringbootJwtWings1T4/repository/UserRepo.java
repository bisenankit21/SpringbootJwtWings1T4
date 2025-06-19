package com.example.SpringbootJwtWings1T4.repository;

import com.example.SpringbootJwtWings1T4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    //to get user by email from database.
    Optional<User> findByEmail(String email);
}
