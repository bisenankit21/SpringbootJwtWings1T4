package com.example.SpringbootJwtWings1T4.service;

import com.example.SpringbootJwtWings1T4.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//As to get all the required method during the process and to supported by UserDeatails we need to implement
//userdetailsService class so that it will handle by USerDetails Service during the process.
//as UserDetails class will interact with User now UerDetailsService will interact with UserService
@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}
