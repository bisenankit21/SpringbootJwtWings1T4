package com.example.SpringbootJwtWings1T4.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="my_user")
/*This User is JPA entity but to make it as a Spring boot user what spring people have done is they given some readymate
of interface as they tell us please implements this interface and I will be scanning this interface
for my spring security and I will know that I have  to perform this function using the interfaces only.
 so to make this JPA user to spring boot user we have to implement this interface that is UserDetails.
 now UserDetails will handle all the operations related to Users and Role earlier it was handle by JPA manager. so it best pracftie
 to implements UserDetails on top of User JPA entity*/
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String email;
    @Column
    private String pass;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public User(int id, String email, String pass) {
        this.id = id;
        this.email = email;
        this.pass = pass;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    //this below authority is just means what are the roles of available for the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public String getPassword() {
        return this.pass;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
