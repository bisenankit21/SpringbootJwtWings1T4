package com.example.SpringbootJwtWings1T4.repository;

import com.example.SpringbootJwtWings1T4.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
}
