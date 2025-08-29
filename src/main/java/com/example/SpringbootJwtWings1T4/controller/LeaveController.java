package com.example.SpringbootJwtWings1T4.controller;

import com.example.SpringbootJwtWings1T4.model.LeaveRequest;
import com.example.SpringbootJwtWings1T4.service.LeaveService;
import com.example.SpringbootJwtWings1T4.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    LeaveService leaveService;

    @PostMapping("/user")
    public ResponseEntity<Object> addLeaveRequest(@RequestHeader("Authorization") String authHeader,
                                                  @RequestBody LeaveRequest leaveReq) {
        //by looking at this request we are not sure who is currently trying to access it so we need to
        // accept token and from that token we will find the user and then the role associate with it,
        //so we are going to extract the header details from the token
        //check what part of header is relevant for us
       String jwt =  authHeader.substring(7);
        //this username we can extract at service layer as well but we are doing it here its fine
       String username = jwtUtil.extractUsername(jwt);
        //now we need to pass this username to service layer as well
       return leaveService.addLeaveRequest(username,leaveReq);
    }

    @PutMapping("/comment")
    public ResponseEntity<Object> updateUserComment(@RequestHeader("Authorization") String authHeader,
                                                    @RequestParam int leaveId,
                                                    @RequestParam String comment) {
        String jwt=  authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.updateUserComment(username,leaveId,comment);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getLeaves(@RequestHeader("Authorization") String authHeader) {
        String jwt=  authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.getLeaves(username);
    }

    @PutMapping("/status")
    public ResponseEntity<Object> updateLeaveStatus(@RequestHeader("Authorization") String authHeader,
                                                    @RequestParam int leaveId,
                                                    @RequestParam String status) {
        String jwt=  authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.updateLeaveStatus(username,leaveId,status);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLeave(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable int id) {
        String jwt=  authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.deleteLeave(username,id);

    }


    @GetMapping("/days")
    public ResponseEntity<Object> getLeavesByNoOfdays(@RequestHeader("Authorization") String authHeader,
                                                      @RequestParam int noOfDays) {
        String jwt=  authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.getLeavesByNoOfdays(username,noOfDays);
    }



}
