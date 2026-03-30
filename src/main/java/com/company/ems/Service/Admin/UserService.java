//package com.company.ems.Service;
//import com.company.ems.Entity.Attendance;
//import com.company.ems.Entity.User;
//import com.company.ems.Repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class UserService {
//    UserRepository userRepository;
//    PasswordEncoder passwordEncoder;
//    public void addUser(User user, String confirmPassword){
//        if(user!=null && confirmPassword!=null) {
//            if (user.getPassword().equals(confirmPassword)) {
//                String email = user.getEmail().trim().toLowerCase();
//                Optional<User> userOpt = userRepository.findByEmail(email);
//                if(userOpt.isEmpty()) {
//                    user.setEmail(email);
//                    user.setPassword(passwordEncoder.encode(user.getPassword()));
//                    userRepository.save(user);
//                }
//            }
//        }
//    }
//    public void editUser(long userId,
//                         String fullName,
//                         String email,
//                         String userName,
//                         String role,
//                         String status){
//
//        Optional<User> userOpt = userRepository.findById(userId);
//
//        if(userOpt.isPresent()){
//            User user = userOpt.get();
//
//            user.setFullName(fullName);
//
//            if(email != null){
//                String normalizedEmail = email.trim().toLowerCase();
//
//                if(!normalizedEmail.equals(user.getEmail())){
//                    Optional<User> userOpt2 = userRepository.findByEmail(normalizedEmail);
//                    if(userOpt2.isPresent() && userOpt2.get().getId() != userId){
//                        // duplicate → do not update
//                        return;
//                    }
//
//                    user.setEmail(normalizedEmail);
//                }
//            }
//
//            if(userName!=null && !userName.isEmpty())
//                user.setUsername(userName);
//
//            if(role!=null && !role.isEmpty())
//                user.setRole(role);
//
//            if(status!=null && !status.isEmpty())
//                user.setStatus(status);
//
//            userRepository.save(user);
//        }
//    }
//    public List<User> getActiveUsersExcept(Long userId){
//        return userRepository.findAll()
//                .stream()
//                .filter(u -> u.getStatus() != null && u.getStatus().equalsIgnoreCase("ACTIVE"))
//                .filter(u -> u.getId() != userId)
//                .toList();
//    }
//    public List<User> getActiveUsers(){
//        return userRepository.findAll()
//                .stream()
//                .filter(u -> u.getStatus() != null && u.getStatus().equalsIgnoreCase("ACTIVE"))
//                .toList();
//    }
//    public void activateUser(long userId){
//        Optional<User> userOpt = userRepository.findById(userId);
//        if(userOpt.isPresent()){
//            User user = userOpt.get();
//            if(user.getStatus()!=null && user.getStatus().equalsIgnoreCase("INACTIVE")) {
//                user.setStatus("ACTIVE");
//                userRepository.save(user);
//            }
//        }
//    }
//    public void deactivateUser(long userId){
//        Optional<User> userOpt = userRepository.findById(userId);
//        if(userOpt.isPresent()){
//            User user = userOpt.get();
//            if(user.getStatus()!=null && user.getStatus().equalsIgnoreCase("ACTIVE")) {
//                user.setStatus("INACTIVE");
//                userRepository.save(user);
//            }
//        }
//    }
//    public void resetPassword(long userId, String newPassword, String confirmPassword){
//        if(newPassword.equals(confirmPassword)) {
//            Optional<User> userOpt = userRepository.findById(userId);
//            if (userOpt.isPresent()) {
//                User user = userOpt.get();
//                user.setPassword(passwordEncoder.encode(newPassword));
//                userRepository.save(user);
//            }
//        }
//    }
//    public Page<User> paginatedUsers(int page, int size){
//
//        if(page < 0) page = 0;
//
//        List<User> users = showAllUsers();
//
//        int start = page * size;
//        int end = Math.min(start + size, users.size());
//
//        List<User> paginatedList;
//
//        if(start < users.size()){
//            paginatedList = users.subList(start, end);
//        } else {
//            paginatedList = new ArrayList<>();
//        }
//
//        return new PageImpl<>(paginatedList, PageRequest.of(page, size), users.size());
//    }
//    public List<User> showAllUsers(){
//        return userRepository.findAll();
//    }
//    public Page<User> filterPaginatedUsers(String search,
//                                           String role,
//                                           String status,
//                                           int page,
//                                           int size){
//        if(page < 0) page = 0;
//
//        List<User> users = filterUsers(search, role, status);
//
//        int start = page * size;
//        int end = Math.min(start + size, users.size());
//
//        List<User> paginatedList;
//
//        if(start < users.size()){
//            paginatedList = users.subList(start, end);
//        } else {
//            paginatedList = new ArrayList<>();
//        }
//
//        return new PageImpl<>(paginatedList, PageRequest.of(page, size), users.size());
//    }
//    public List<User> filterUsers(String search, String role, String status){
//        List<User> list = userRepository.findAll();
//        if(search!=null && !search.trim().isEmpty()) {
//            search = search.trim();
//            String finalSearch = search;
//            list = list.stream().filter(u -> (u.getFullName()!=null && u.getFullName().toLowerCase().contains(finalSearch.toLowerCase())) ||
//                    (u.getEmail()!=null && u.getEmail().toLowerCase().contains(finalSearch.toLowerCase()))).toList();
//        }
//        if(role!=null && !role.equalsIgnoreCase("ALL"))
//            list = list.stream().filter(u -> u.getRole()!=null && u.getRole().equalsIgnoreCase(role)).toList();
//        if(status!=null && !status.equalsIgnoreCase("ALL"))
//            list = list.stream().filter(u -> u.getStatus()!=null && u.getStatus().equalsIgnoreCase(status)).toList();
//        return list;
//    }
//    public List<User> getAllUsers(){
//        return userRepository.findAll();
//    }
//    public User getUserById(long userId){
//        Optional<User> userOpt = userRepository.findById(userId);
//        return userOpt.orElse(null);
//    }
//}






package com.company.ems.Service.Admin;

import com.company.ems.Entity.User;
import com.company.ems.Exception.InvalidInputException;
import com.company.ems.Exception.ResourceNotFoundException;
import com.company.ems.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public void addUser(User user, String confirmPassword){

        if(user == null){
            throw new InvalidInputException("User cannot be null");
        }

        if(confirmPassword == null){
            throw new InvalidInputException("Confirm password is required");
        }

        if(!user.getPassword().equals(confirmPassword)){
            throw new InvalidInputException("Passwords do not match");
        }

        String email = user.getEmail().trim().toLowerCase();

        if(userRepository.findByEmail(email).isPresent()){
            throw new InvalidInputException("Email already exists");
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getStatus()!=null && user.getStatus().equalsIgnoreCase("ACTIVE")) {
            user.setJoiningDate(LocalDate.now());
            user.setLeavingDate(null);
        }
        if(user.getStatus()!=null && user.getStatus().equalsIgnoreCase("INACTIVE")){
            user.setJoiningDate(null);
            user.setLeavingDate(null);
        }

        userRepository.save(user);
    }

    public void editUser(long userId,
                         String fullName,
                         String email,
                         String userName,
                         String role,
                         String status){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFullName(fullName);

        if(email != null){

            String normalizedEmail = email.trim().toLowerCase();

            if(!normalizedEmail.equals(user.getEmail())){

                if(userRepository.findByEmail(normalizedEmail).isPresent()){
                    throw new InvalidInputException("Email already exists");
                }

                user.setEmail(normalizedEmail);
            }
        }

        if(userName != null && !userName.isEmpty()){
            user.setUsername(userName);
        }

        if(role != null && !role.isEmpty()){
            user.setRole(role);
        }

        if(status != null && !status.isEmpty()){
            // CASE 1: INACTIVE → ACTIVE (User joining first time)
            if(user.getStatus().equalsIgnoreCase("INACTIVE") && status.equalsIgnoreCase("ACTIVE")){
                user.setJoiningDate(LocalDate.now());
                user.setLeavingDate(null);
            }

            // CASE 2: ACTIVE → INACTIVE (User leaving)
            else if(user.getStatus().equalsIgnoreCase("ACTIVE") && status.equalsIgnoreCase("INACTIVE")){
                user.setLeavingDate(LocalDate.now());
            }
            user.setStatus(status);
        }

        userRepository.save(user);
    }

    public List<User> getActiveUsersExcept(Long userId){

        if(userId == null){
            throw new InvalidInputException("UserId cannot be null");
        }

        return userRepository.findAllByRole("EMPLOYEE")
                .stream()
                .filter(u -> u.getStatus()!=null && u.getStatus().equalsIgnoreCase("ACTIVE"))
                .filter(u -> u.getId() != userId)
                .toList();
    }

    public List<User> getActiveUsers(){
        return userRepository.findAllByRole("EMPLOYEE")
                .stream()
                .filter(u -> u.getStatus()!=null && u.getStatus().equalsIgnoreCase("ACTIVE"))
                .toList();
    }

    public void activateUser(long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(user.getStatus()!=null && user.getStatus().equalsIgnoreCase("ACTIVE")){
            throw new InvalidInputException("User is already active");
        }

        user.setStatus("ACTIVE");
        if(user.getJoiningDate() == null){
            user.setJoiningDate(LocalDate.now());
        }
        user.setLeavingDate(null);
        userRepository.save(user);
    }

    public void deactivateUser(long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(user.getStatus()!=null && user.getStatus().equalsIgnoreCase("INACTIVE")){
            throw new InvalidInputException("User is already inactive");
        }

        user.setStatus("INACTIVE");
        user.setLeavingDate(LocalDate.now());
        userRepository.save(user);
    }

    public void resetPassword(long userId, String newPassword, String confirmPassword){

        if(newPassword == null || confirmPassword == null){
            throw new InvalidInputException("Password fields cannot be empty");
        }

        if(!newPassword.equals(confirmPassword)){
            throw new InvalidInputException("Passwords do not match");
        }

        if(newPassword.length() < 6){
            throw new InvalidInputException("Password must be at least 6 characters");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public Page<User> paginatedUsers(int page, int size){

        if(page < 0) page = 0;

        List<User> users = showAllUsers();

        int start = page * size;
        int end = Math.min(start + size, users.size());

        List<User> paginatedList;

        if(start < users.size()){
            paginatedList = users.subList(start, end);
        } else {
            paginatedList = new ArrayList<>();
        }

        return new PageImpl<>(paginatedList, PageRequest.of(page, size), users.size());
    }

    public List<User> showAllUsers(){
        return userRepository.findAllByRole("EMPLOYEE");
    }

    public Page<User> filterPaginatedUsers(String search,
                                           String role,
                                           String status,
                                           int page,
                                           int size){

        if(page < 0) page = 0;

        List<User> users = filterUsers(search, role, status);

        int start = page * size;
        int end = Math.min(start + size, users.size());

        List<User> paginatedList;

        if(start < users.size()){
            paginatedList = users.subList(start, end);
        } else {
            paginatedList = new ArrayList<>();
        }

        return new PageImpl<>(paginatedList, PageRequest.of(page, size), users.size());
    }

    public List<User> filterUsers(String search, String role, String status){

        List<User> list = userRepository.findAllByRole("EMPLOYEE");

        if(search != null){
            search = search.trim().toLowerCase();

            if(!search.isEmpty()){
                String finalSearch = search;

                list = list.stream()
                        .filter(u ->
                                (u.getFullName()!=null && u.getFullName().toLowerCase().contains(finalSearch)) ||
                                        (u.getEmail()!=null && u.getEmail().toLowerCase().contains(finalSearch))
                        )
                        .toList();
            }
        }

        if(role != null && !role.equalsIgnoreCase("ALL")){
            list = list.stream()
                    .filter(u -> u.getRole()!=null && u.getRole().equalsIgnoreCase(role))
                    .toList();
        }

        if(status != null && !status.equalsIgnoreCase("ALL")){
            list = list.stream()
                    .filter(u -> u.getStatus()!=null && u.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        return list;
    }

    public List<User> getAllUsers(){
        return userRepository.findAllByRole("EMPLOYEE");
    }

    public User getUserById(long userId){

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}