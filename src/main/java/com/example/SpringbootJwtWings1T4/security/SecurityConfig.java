package com.example.SpringbootJwtWings1T4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.DataInput;

@Configuration
public class SecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;

//now we need to add that authentication filter in the security filter chain as well and also need to authorize the request
    //all the public and secured endpoint
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.build();
    }




   @Bean
   PasswordEncoder passwordEncoder(){
    //just return the default password encoder
    return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
       /*as we know DaoAuthenticationProvider will handle the authentication and
         take help from Password Encoder and USerDetailsService,
        userDetailsService will help us to get the user details from db and
        passwordEncoder will help us to encrypt the password*/
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //return the daoAuthenticationProvider which will interact with database.
        return daoAuthenticationProvider;
    }

    //we need authentication manager as well which will internally interact with authentication provider
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       //default authentication manager is provider manager. Provider Manager is the implementation of AuthenticationManager
       return authenticationConfiguration.getAuthenticationManager();
    }

    //we can do same thing like this , try with this as well
  /*  @Bean
    AuthenticationManager authenticationManager(){
       AuthenticationManager authenticationManager = new ProviderManager(authenticationProvider());
       return authenticationManager;
    }
*/

}
