//package com.company.ems.Service;
//
//import com.company.ems.Entity.User;
//import com.company.ems.Repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class SettingsService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    public Long getCurrentUserId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
//            throw new RuntimeException("User not authenticated");
//        }
//        String email = auth.getName();
//        return userRepository.findByEmail(email)
//                .map(User::getId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//    public void changePassword(String currentPassword,
//                               String newPassword,
//                               String confirmPassword){
//        Long userId = getCurrentUserId();
//        if(userId == null) return;
//        Optional<User> userOpt = userRepository.findById(userId);
//        if(userOpt.isPresent()){
//            User user = userOpt.get();
//            if(!passwordEncoder.matches(currentPassword, user.getPassword())){
//                return;
//            }
//            if(!newPassword.equals(confirmPassword)){
//                return;
//            }
//            user.setPassword(passwordEncoder.encode(newPassword));
//            userRepository.save(user);
//        }
//    }
//    public User getUser(){
//        Optional<User> userOpt = userRepository.findById(getCurrentUserId());
//        return userOpt.orElse(null);
//    }
//}



package com.company.ems.Service.Admin;

import com.company.ems.Entity.User;
import com.company.ems.Exception.InvalidInputException;
import com.company.ems.Exception.ResourceNotFoundException;
import com.company.ems.Exception.UnauthorizedException;
import com.company.ems.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long getCurrentUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new UnauthorizedException("User not authenticated");
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void changePassword(String currentPassword,
                               String newPassword,
                               String confirmPassword){

        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!passwordEncoder.matches(currentPassword, user.getPassword())){
            throw new InvalidInputException("Current password is incorrect");
        }

        if(!newPassword.equals(confirmPassword)){
            throw new InvalidInputException("New password and confirm password do not match");
        }

        if(newPassword.length() < 6){
            throw new InvalidInputException("Password must be at least 6 characters long");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getUser(){

        Long userId = getCurrentUserId();

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}