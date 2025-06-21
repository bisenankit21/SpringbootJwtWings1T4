package com.example.SpringbootJwtWings1T4.security;

import com.example.SpringbootJwtWings1T4.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

/*The role of this authentication filter is accept the incoming request and check whether the rquest has
* token or not, extract that token if you find it and if that is the JWT or BEARER token then it will process the request
* or if that request has no JWT token then it will direct pass it to the next filter in filter chain and not process it, it will process only when the incoming request has JWT token*/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //Extract the auth header from the request
        String authHeader = request.getHeader("Authorization");
        if(authHeader ==null || !authHeader.startsWith("Bearer ")){
            //proceed with remaining filter
            filterChain.doFilter(request,response);
            return;
        }
        //JWT purpose
        String jwtToken=authHeader.substring(7);
        //Extract username(EmailId)
        String username=jwtUtil.extractUsername(jwtToken);
        /*UsernamePasswordAuthenticationToken is contained the information about the user and it
        also create the authentication object this authentication object is stored in the class
        SecurityContextHolder which is responsible to hold the authentication object.
        so first we need to check that the particular user is authenticate or not.
        if this SecurityContextHolder.getContext().getAuthentication()==null it means we can say our user is not
        authenticated so we need to make it authenticated*/

        //User not authenticated
        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDb = userDetailsService.loadUserByUsername(username);
            //validate the token
            if(userDb !=null && jwtUtil.validateToken(jwtToken,userDb)){
                //Register my current user in the security context
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDb,null, userDb.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetails(request));  //non mendate
                //update my user as valid user
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        //if the user is already authenticated then no need to make it authenticate just pass it to the filter chain

        //continue filter chain
        filterChain.doFilter(request,response);
    }
}
