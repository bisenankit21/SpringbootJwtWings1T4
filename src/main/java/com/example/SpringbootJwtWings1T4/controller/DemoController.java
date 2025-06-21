package com.example.SpringbootJwtWings1T4.controller;

import com.example.SpringbootJwtWings1T4.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {
    @Autowired
    JwtUtil jwtUtil;
    @GetMapping("/hello")
    public String getMethodName(@RequestHeader("Authorization") String authHeader) {
        String Username = jwtUtil.extractUsername(authHeader.substring(7));
        return "hello to :" + Username;
    }
}
