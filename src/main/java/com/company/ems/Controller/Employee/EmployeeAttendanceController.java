package com.company.ems.Controller.Employee;

import com.company.ems.Entity.Attendance;
import com.company.ems.Entity.User;
import com.company.ems.Repository.UserRepository;
import com.company.ems.Service.Employee.EmployeeAttendanceService;
import com.company.ems.Service.Employee.EmployeeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@Controller
@RequestMapping("/employee")
public class EmployeeAttendanceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeAttendanceService attendanceService;

    @Autowired
    private EmployeeTaskService taskService;

    @GetMapping("/attendance")
    public String attendancePage(Model model, Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long userId = user.getId();

        Attendance todayAttendance = attendanceService.getTodayAttendance(userId);

        List<Attendance> attendanceHistory =
                attendanceService.getAttendanceHistory(userId, user.getJoiningDate());

        long pendingTasks = taskService.getPendingTasks(userId);

        model.addAttribute("todayAttendance", todayAttendance);
        model.addAttribute("attendanceHistory", attendanceHistory);
        model.addAttribute("pendingTasks", pendingTasks);

        return "Employee_dashboard/Attendance/attendance";
    }
    @PostMapping("/attendance/check-in")
    @ResponseBody
    public String checkIn(Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.checkIn(user.getId(), user);

        return "Checked In";
    }
    @PostMapping("/attendance/check-out")
    @ResponseBody
    public String checkOut(Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.checkOut(user.getId());

        return "Checked Out";
    }
    @GetMapping("/attendance/filter")
    @ResponseBody
    public List<Attendance> filterAttendance(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Principal principal){

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return attendanceService.filterAttendance(
                user.getId(),
                user.getJoiningDate(),
                status,
                month,
                year
        );
    }
}