package com.example.SpringbootJwtWings1T4.controller;

import com.example.SpringbootJwtWings1T4.model.LoginRequest;
import com.example.SpringbootJwtWings1T4.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest loginRequest) {
        return loginService.loginUser(loginRequest);

    }
}
