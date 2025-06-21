package com.example.SpringbootJwtWings1T4.security;

import com.example.SpringbootJwtWings1T4.config.AuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    AuthEntryPoint authEntryPoint;

    private final String[] WHITE_LIST_URL={"/login", "/h2-console/**"};

    /* This below method  indicates that requests matching the pattern /h2-console*//** should
     be ignored by the security filters. This is commonly used to allow access to the H2 database
     console without authentication.*/

    //This we no need to write in the main exam
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }

    //now we need to add that authentication filter in the security filter chain as well and also need to authorize the request
    //all the public and secured endpoint
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //Step 1: Disable CSRF Protection
        httpSecurity.csrf(csrf->csrf.disable());

        //as we don't want to save any information of the user in the application  we are saving it in our database only and security Context but we are not storing
        //any cookies or sessionInformation in the application so we need to change the sessionManagement policy
        //step2: Session Management
        httpSecurity.sessionManagement(
                sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //Step 3: Register our custom authentication filter
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //Step4: Register
        //Public handler- for this we have created bean for that liek Authentication manager or Authentication provider
        //so we need to autowire this as well as it will be handle public APIs so we need to register this as well
        httpSecurity.authenticationProvider(authenticationProvider());

        //protected resources
        httpSecurity.authorizeHttpRequests(httpConfig->{
            //the request which are matching with WHITE_LIST_URL will be permitted and rest any other request need to be authenticated
            httpConfig.requestMatchers(WHITE_LIST_URL).permitAll()
                    .requestMatchers("/api/**").hasAnyAuthority("USER")
                    .requestMatchers(HttpMethod.POST,"/leave/user").hasAnyAuthority("USER")
                    .requestMatchers(HttpMethod.GET,"/leave/user").hasAnyAuthority("USER","ADMIN")
                    .requestMatchers(HttpMethod.GET,"/leave/comment").hasAnyAuthority("USER")
                    .requestMatchers(HttpMethod.PUT,"/leave/status").hasAnyAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/leave").hasAnyAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/leave/days").hasAnyAuthority("ADMIN")
                    .anyRequest().authenticated();
        });
        httpSecurity.exceptionHandling(exceptionConfig->exceptionConfig.authenticationEntryPoint(authEntryPoint));
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
