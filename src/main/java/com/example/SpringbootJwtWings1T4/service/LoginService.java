package com.example.SpringbootJwtWings1T4.service;

import com.example.SpringbootJwtWings1T4.model.LoginRequest;
import com.example.SpringbootJwtWings1T4.model.LoginResponse;
import com.example.SpringbootJwtWings1T4.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    //to check the user is present inside DB or not we have userDetailService layer
    @Autowired
    UserDetailsService userDetailsService;
    //Need to generate the token so we have JwtUtil class
    @Autowired
    JwtUtil jwtUtil;
    //we know for public endpoint Authentication Manager or Authentication Provider is going to work
    @Autowired
    AuthenticationProvider authenticationProvider;

    public ResponseEntity<Object> loginUser(LoginRequest loginRequest) {
        try {
            //here we need to check the incoming user is present in Db or not if it exist it provide
            // correct password or not and if providing the correct password then return jwt token for
            // that user Fetch the user by Username: Dir it validate the pass ? No
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmailId());
            //Everything we talk about security at user means saving the username in the context we talk
            // about only one class named SecurityContext
            // and how does the Security Context registered the user and it does this using the token
/*Note : As we know that UsernamePasswordAutnentication filter extract the username and password from
 the request and create an authentication token, and then it will pass it to the Authentication
provider for the further validation , it will encode the password and do all the required steps
 like getting from db and password matching everything we need to do this here*/
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmailId(),
                    loginRequest.getPassword());

            //This guys will validate the password: we should use this below line to validate the tone as
            //we already passed USerDetailsService and PasswordEncoder during bean creation
            //now the Authentication provider will authenticate the user using this above token.
            //in our Security Architecture as well we learn than we pass the authentication token to the
            // Authentication Provider and it will take care of the further validation
            authenticationProvider.authenticate(authenticationToken);
            //now we need to generate the token
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
        }
        catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Does not exist");
        }
    }
}
