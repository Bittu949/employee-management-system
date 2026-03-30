package com.company.ems.Controller.Admin;

import com.company.ems.Exception.InvalidPasswordException;
import com.company.ems.Exception.ResourceNotFoundException;
import com.company.ems.Service.Admin.SettingsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/settings")
public class SettingsController {

    SettingsService settingsService;

    @GetMapping("/page")
    public String settingsPage(Model model) {

        var user = settingsService.getUser();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        model.addAttribute("user", user);
        return "Admin_dashboard/Settings/settings";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) {

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidPasswordException("New password cannot be empty");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        settingsService.changePassword(currentPassword, newPassword, confirmPassword);

        return "redirect:/dashboard";
    }
}