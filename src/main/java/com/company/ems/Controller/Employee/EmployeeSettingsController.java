package com.company.ems.Controller.Employee;

import com.company.ems.Dto.ChangePasswordRequest;
import com.company.ems.Entity.User;
import com.company.ems.Repository.TaskRepository;
import com.company.ems.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Controller
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeSettingsController {
    UserRepository userRepository;
    TaskRepository taskRepository;
    PasswordEncoder passwordEncoder;
    @GetMapping("/settings")
    public String settingsPage(Model model, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        long userId = user.getId();
        long pendingTasks = taskRepository.countByUserIdAndStatus(userId, "Pending");
        model.addAttribute("user", user);
        model.addAttribute("pendingTasks", pendingTasks);
        return "Employee_dashboard/Settings/settings";
    }
    @PostMapping("/change-password")
    @ResponseBody
    public String changePassword(@RequestBody ChangePasswordRequest request,
                                 Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return "Current password is incorrect";
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "New password and confirm password do not match";
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password updated successfully";
    }
}
