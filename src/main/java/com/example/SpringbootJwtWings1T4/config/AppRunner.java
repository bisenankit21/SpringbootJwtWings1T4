package com.example.SpringbootJwtWings1T4.config;

import com.example.SpringbootJwtWings1T4.model.Role;
import com.example.SpringbootJwtWings1T4.model.User;
import com.example.SpringbootJwtWings1T4.repository.RoleRepo;
import com.example.SpringbootJwtWings1T4.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//This Application runner it runs by default as soon as the application starts.
//if we requered default task which needs to be run at the start of your application we can use this
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role role1 =  new Role();
        role1.setRoleName("ADMIN");
        Role role2 =  new Role();
        role2.setRoleName("USER");

        //Saving the default roles
        Role SaveRole1 = roleRepo.save(role1);
        Role SaveRole2 =  roleRepo.save(role2);

        //Create a User
        User user1 = new User();
        user1.setEmail("ankit@gmail.com");
        user1.setPass("123");
        user1.setRole(SaveRole1);

        User user2 = new User();
        user2.setEmail("Ram@gmail.com");
        user2.setPass("456");
        user2.setRole(SaveRole2);

        User user3 = new User();
        user3.setEmail("Hari@gmail.com");
        user3.setPass("789");
        user3.setRole(SaveRole2);

        //Saving the default users
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
    }
}
