package ru.bsuedu.cad.lab4.persistence;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskDetailsController {
    private final DatabaseTaskStore tasks;

    TaskDetailsController(DatabaseTaskStore tasks) {
        this.tasks = tasks;
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable long id, Model model, HttpServletRequest request) {
        model.addAttribute("details", tasks.details(id));
        model.addAttribute("canManage", request.isUserInRole("MODERATOR"));
        model.addAttribute("priorities", Priority.values());
        return "task-details";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/details/{id}/settings")
    public String settings(@PathVariable long id, @RequestParam Priority priority, @RequestParam String category) {
        tasks.settings(id, priority, category);
        return "redirect:/details/" + id;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/details/{id}/subtasks")
    public String addSubtask(@PathVariable long id, @RequestParam String description) {
        tasks.addSubtask(id, description);
        return "redirect:/details/" + id;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/details/{id}/subtasks/{subtaskId}")
    public String completeSubtask(@PathVariable long id, @PathVariable long subtaskId, @RequestParam boolean completed) {
        tasks.completeSubtask(id, subtaskId, completed);
        return "redirect:/details/" + id;
    }
}
