package com.company.ems.Controller.Employee;

import com.company.ems.Entity.Task;
import com.company.ems.Entity.User;
import com.company.ems.Repository.UserRepository;
import com.company.ems.Service.Employee.EmployeeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeTaskController {
    @Autowired
    private EmployeeTaskService taskService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/tasks")
    public String myTasks(Model model, Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        List<Task> tasks = taskService.getTasksByUser(userId);

        long total = taskService.getTotalTasks(userId);
        long completed = taskService.getCompletedTasks(userId);
        long pending = taskService.getPendingTasks(userId);

        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTasks", total);
        model.addAttribute("completedTasks", completed);
        model.addAttribute("pendingTasks", pending);

        return "Employee_dashboard/My_Tasks/my_tasks";
    }
    @PostMapping("/task/update-status")
    @ResponseBody
    public String updateStatus(@RequestParam Long taskId,
                               @RequestParam String status,
                               Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        taskService.updateTaskStatus(taskId, status, user.getId());

        return "Updated";
    }
    @GetMapping("/task/{id}")
    public String viewTask(@PathVariable Long id, Model model, Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskService.getTaskById(id);

        if (task.getUser() == null || task.getUser().getId() != user.getId()) {
            throw new RuntimeException("Unauthorized access");
        }

        long pendingTasks = taskService.getPendingTasks(user.getId());

        model.addAttribute("task", task);
        model.addAttribute("pendingTasks", pendingTasks);

        return "Employee_dashboard/My_Tasks/View/view";
    }
    @GetMapping("/tasks/filter")
    @ResponseBody
    public List<Task> filterTasks(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) String status,
                                  @RequestParam(required = false) String priority,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Principal principal){

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskService.filterTasks(user, search, status, priority);
    }
}
