package com.company.ems.Controller.Employee;

import com.company.ems.Entity.User;
import com.company.ems.Repository.UserRepository;
import com.company.ems.Service.Employee.EmployeeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
@RequestMapping("/employee")
public class EmployeeProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeTaskService taskService;
    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long pendingTasks = taskService.getPendingTasks(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("pendingTasks", pendingTasks);

        return "Employee_dashboard/Profile/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                Principal principal) {

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        String currentEmail = principal.getName();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(fullName.trim());

        userRepository.save(user);

        return "redirect:/employee/profile";
    }
}
