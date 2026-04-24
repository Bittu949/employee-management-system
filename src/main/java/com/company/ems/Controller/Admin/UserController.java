package com.company.ems.Controller.Admin;

import com.company.ems.Entity.User;
import com.company.ems.Exception.InvalidDataException;
import com.company.ems.Exception.ResourceNotFoundException;
import com.company.ems.Service.Admin.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/user")
public class UserController {

    UserService userService;

    @GetMapping("/addUserPage")
    public String addUserPage() {
        return "Admin_dashboard/Manage_users/Add_user/add_user";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String userName,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam String role,
                          @RequestParam String status) {

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidDataException("Full name is required");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("Email is required");
        }

        if (password == null || password.isEmpty()) {
            throw new InvalidDataException("Password cannot be empty");
        }

        if (!password.equals(confirmPassword)) {
            throw new InvalidDataException("Passwords do not match");
        }

        userService.addUser(
                new User(fullName, email, userName, password, role, status),
                confirmPassword
        );

        return "redirect:/admin/user/allUsers";
    }

    @GetMapping("/{userId}/editPage")
    public String displayEditUserPage(@PathVariable long userId,
                                      Model model) {

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        model.addAttribute("user", user);

        return "Admin_dashboard/Manage_users/Edit_user/edit_user";
    }

    @PostMapping("/{userId}/edit")
    public String editUser(@PathVariable long userId,
                           @RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String role,
                           @RequestParam(required = false) String status) {

        userService.editUser(userId, fullName, email, username, role, status);

        return "redirect:/admin/user/allUsers";
    }

    @PostMapping("/{userId}/activate")
    public String activateUser(@PathVariable long userId) {

        userService.activateUser(userId);

        return "redirect:/admin/user/allUsers";
    }

    @PostMapping("/{userId}/deactivate")
    public String deactivateUser(@PathVariable long userId) {

        userService.deactivateUser(userId);

        return "redirect:/admin/user/allUsers";
    }

    @GetMapping("/{userId}/resetPasswordPage")
    public String resetPasswordPage(@PathVariable long userId,
                                    Model model) {

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        model.addAttribute("user", user);

        return "Admin_dashboard/Manage_users/Reset_password/reset_password";
    }

    @PostMapping("/{userId}/resetPassword")
    public String resetPassword(@PathVariable long userId,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword) {

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidDataException("Password cannot be empty");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidDataException("Passwords do not match");
        }

        userService.resetPassword(userId, newPassword, confirmPassword);

        return "redirect:/admin/user/allUsers";
    }

    @GetMapping("/allUsers")
    public String displayAllPaginatedUsers(Model model,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {

        Page<User> paginatedUsers = userService.paginatedUsers(page, size);

        model.addAttribute("users", paginatedUsers.getContent());
        model.addAttribute("currentPage", paginatedUsers.getNumber());
        model.addAttribute("totalPages", paginatedUsers.getTotalPages());

        return "Admin_dashboard/Manage_users/manage_users";
    }

    @GetMapping("/filter")
    @ResponseBody
    public Page<User> filterPaginatedUsers(@RequestParam(required = false) String search,
                                           @RequestParam(required = false) String role,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {

        return userService.filterPaginatedUsers(search, role, status, page, size);
    }
}