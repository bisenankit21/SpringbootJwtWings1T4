package com.example.SpringbootJwtWings1T4.repository;

import com.example.SpringbootJwtWings1T4.model.Leave;
import com.example.SpringbootJwtWings1T4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, Integer> {
    List<Leave> findByUser(User user);
    List<Leave> findByUserEmail(String email);
    List<Leave> findByUserId(int id);

    List<Leave> findByNoOfDaysGreaterThan(int noOfDays);
}
