package com.company.ems.Security;

import com.company.ems.Entity.User;
import com.company.ems.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {

        String email = System.getenv("ADMIN_EMAIL");
        String password = System.getenv("ADMIN_PASSWORD");
        String username = System.getenv("ADMIN_USERNAME");

        if (email == null || password == null || username == null) {
            throw new RuntimeException("Admin environment variables are not set");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        User user = new User(
                "Admin",
                email,
                username,
                passwordEncoder.encode(password),
                "ADMIN",
                "ACTIVE"
        );

        userRepository.save(user);
        System.out.println("Admin account created");
    }
}