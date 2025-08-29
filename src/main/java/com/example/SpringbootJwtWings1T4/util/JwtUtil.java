package com.example.SpringbootJwtWings1T4.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;

@Component

//this will work for Spring boot 2.x and 3.x as well.
@SuppressWarnings("deprecation")   //this annotation just suppress the warning caused by the deprecated method nothing else.
public class JwtUtil {

   // @Autowired
   // UserDetailsService userDetailsService;

    //in the latest spring boot we need this secret key have more then 256 bits length so just replace the
    // given
    // secret key with this value
    private final static String SECRET_KEY = "ThisIsASecretKeyThisIsASecretKeyThisIsASecretKey";

    private final static long EXPIRATION_TIME = 24*60*60*1001;//in millisecond

  //Decoding or Parsing part

    public Claims extractAllClaims(String token){
        //once you are extracting you are parsing the token and once you are creating the you will be using
        // builder
        //secondly we need to set the secret key even if we are creating the token or extracting from the
        // token
        //in the latest springboot version we need to build the parser
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
    //now from the above claim we need to extract username as well
    public String extractUsername(String token){
        Claims claim = extractAllClaims(token);
        String username = claim.getSubject();
        return username;
    }

    //also want to get my expiry, these ae the utility methods which good to have
    public Date extractExpiry(String token){
        Claims claim =  extractAllClaims(token);
        Date expiry = claim.getExpiration();
        return expiry;
    }

    //Encoding or generation part
    public String generateToken(UserDetails userDetails){
        //JWTs is for JWT service
        //we are using HS256 because our secret key is 256 bit key
        return Jwts.builder().signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                //as we dont need extra claim so just pass the empty has set to the claims or we also can
                // skip this but betterr to use this
                .addClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                //we need to compact it as it will be big string so need to compact that
                .compact();
    }

    //Validation of the token
    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        Date expiry = extractExpiry(token);
        //we can do one other way as we can check this user name is present in the database or not using the
        //userDetailsService by calling loadUserByUsername method.
        //expiry should not cross the current time if that cross the current time that means that token got expired.
        //so expiry should be in after the current time
        //this below will check if the expiry date is after the current time
       return (username.equals(userDetails.getUsername())
               && expiry.after(new Date(System.currentTimeMillis())));
    }

   /* public static void main(String[] args) {
        UserDetails user = new User("Ankit","123",List.of(new SimpleGrantedAuthority("ADMIN")));
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken(user);
        System.out.println("Token is "+token);
        System.out.println("username from token "+jwtUtil.extractUsername(token));
        System.out.println("Expiry date from token "+jwtUtil.extractExpiry(token));
        System.out.println("Validation " +jwtUtil.validateToken(token, user));

    }*/

}
