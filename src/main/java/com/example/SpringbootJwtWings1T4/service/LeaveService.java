package com.example.SpringbootJwtWings1T4.service;

import com.example.SpringbootJwtWings1T4.model.*;
import com.example.SpringbootJwtWings1T4.repository.LeaveRepo;
import com.example.SpringbootJwtWings1T4.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LeaveService {
    @Autowired
    LeaveRepo leaveRepo;

    @Autowired
    UserRepo userRepo;

    public ResponseEntity<Object> addLeaveRequest(String username, LeaveRequest leaveReq) {
        try{
           Optional<User> userOpn= getUserFromUsername(username);
           if(userOpn.isEmpty()){
               //Sanity Check #1 : user Does not exists.
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: LeaveRequest");
           }
            //Sanity check no.2 : this will tell this user have which role. Role Sanity check
            User userDb = userOpn.get();
            Role  role =  userDb.getRole();
            if(!role.getRoleName().equals("USER")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized User: LeaveRequest");
            }
            Leave leave = new Leave();
            leave.setApplied_on(leaveReq.getApplied_on());
            leave.setCategory(leaveReq.getCategory());
            leave.setDescription(leaveReq.getDescription());
            leave.setNoOfDays(leaveReq.getNoOfDays());
            leave.setStatus(Status.PENDING);
            leave.setUser(userDb);
            Leave savedLeave = leaveRepo.save(leave);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLeave);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server"+e.getMessage());
        }

    }

    public ResponseEntity<Object> updateUserComment(String username, int leaveId, String comment) {
        try {
            Optional<User> userOpn = getUserFromUsername(username);
            if (userOpn.isEmpty()) {
                //Sanity Check #1 : user Does not exists.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: LeaveRequest");
            }
            //Sanity check no.2 : this will tell this user have which role. Role Sanity check
            User userDb = userOpn.get();
            Role role = userDb.getRole();
            if (!role.getRoleName().equals("USER")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized User: LeaveRequest");
            }
            //need to check that leavedId is present or not or if present then it is belong to this user or not
            Optional<Leave> leaveOpn = leaveRepo.findById(leaveId);
            if (leaveOpn.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave does not exists: LeaveRequest");

            }
            Leave leave = leaveOpn.get();
            User user = leave.getUser();
            if(!userDb.getEmail().equals(user.getEmail())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("you do not own this leave : LeaveRequest");

            }
            //update the comment
            leave.setDescription(comment);
            Leave savedLeave = leaveRepo.save(leave);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLeave);
        }
        catch(Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server" + e.getMessage());
            }
    }

    public ResponseEntity<Object> getLeaves(String username) {
        try{
            Optional<User> userOpn= getUserFromUsername(username);
            if(userOpn.isEmpty()){
                //Sanity Check #1 : user Does not exists.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: LeaveRequest");
            }
            //Sanity check no.2 : this will tell this user have which role. Role Sanity check
            User user = userOpn.get();
            Role  role =  user.getRole();
            if(role.getRoleName().equals("ADMIN")){
                return ResponseEntity.status(HttpStatus.OK).body(leaveRepo.findAll());
            } else if (role.getRoleName().equals("USER")) {
                List<Leave> customeLeaves = leaveRepo.findByUserEmail(username);
                return ResponseEntity.status(HttpStatus.OK).body(customeLeaves);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Unauthorized: LeaveRequest");
            }

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server"+e.getMessage());
        }
    }

    public ResponseEntity<Object> updateLeaveStatus(String username, int leaveId, String status) {
        try{
            Optional<User> userOpn= getUserFromUsername(username);
            if(userOpn.isEmpty()){
                //Sanity Check #1 : user Does not exists.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: LeaveRequest");
            }
            //Sanity check no.2 : this will tell this user have which role. Role Sanity check
            User user = userOpn.get();
            Role  role =  user.getRole();
            if(!role.getRoleName().equals("ADMIN")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized User: LeaveRequest");
            }
            Optional<Leave> leaveOpn = leaveRepo.findById(leaveId);
            if (leaveOpn.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave does not exists: LeaveRequest");

            }
            Leave leave = leaveOpn.get();
            if(!(status.equalsIgnoreCase("APPROVED") || status.equalsIgnoreCase("REJECTED"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalida Status: LeaveRequest");
            }
            leave.setStatus(Status.valueOf(status.toUpperCase()));
            leaveRepo.save(leave);
            return ResponseEntity.status(HttpStatus.OK).body("Leave Status updated successfully" + status.toUpperCase());

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server"+e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteLeave( String username, int id) {
        try{
            Optional<User> userOpn= getUserFromUsername(username);
            if(userOpn.isEmpty()){
                //Sanity Check #1 : user Does not exists.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: LeaveRequest");
            }
            //Sanity check no.2 : this will tell this user have which role. Role Sanity check
            User user = userOpn.get();
            Role  role =  user.getRole();
            if(!role.getRoleName().equals("ADMIN")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized User: LeaveRequest");
            }
            Optional<Leave> leaveOpn = leaveRepo.findById(id);
            if (leaveOpn.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave does not exists: LeaveRequest");
            }
            Leave leave = leaveOpn.get();
            leaveRepo.delete(leave);
            return ResponseEntity.status(HttpStatus.OK).body("Leave deleted successfully");

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server"+e.getMessage());
        }
    }

    public ResponseEntity<Object> getLeavesByNoOfdays(String username, int noOfDays) {
        try{
            Optional<User> userOpn= getUserFromUsername(username);
            if(userOpn.isEmpty()){
                //Sanity Check #1 : user Does not exists.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: LeaveRequest");
            }
            //Sanity check no.2 : this will tell this user have which role. Role Sanity check
            User user = userOpn.get();
            Role  role =  user.getRole();
            if(!role.getRoleName().equals("ADMIN")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized User: LeaveRequest");
            }
            List<Leave> leaveCondn = leaveRepo.findByNoOfDaysGreaterThan(noOfDays);
            Set<String> userNames =  new HashSet<>();
            for(Leave leave : leaveCondn){
                userNames.add(leave.getUser().getEmail());
            }
            return ResponseEntity.status(HttpStatus.OK).body(userNames);

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server"+e.getMessage());
        }
    }

    private Optional<User> getUserFromUsername(String enailId) {
       return userRepo.findByEmail(enailId);
    }
}
