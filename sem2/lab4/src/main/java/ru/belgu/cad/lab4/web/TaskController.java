package ru.belgu.cad.lab4.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.belgu.cad.lab4.domain.Priority;
import ru.belgu.cad.lab4.domain.Task;
import ru.belgu.cad.lab4.service.TaskService;

/** Веб-интерфейс списка задач: данные читаются и пишутся в PostgreSQL. */
@Controller
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String category,
                        @RequestParam(required = false) Priority priority,
                        @RequestParam(required = false) Boolean completed,
                        Model model, Authentication auth) {
        model.addAttribute("tasks", service.find(category, priority, completed));
        model.addAttribute("categories", service.categories());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("selectedCompleted", completed);
        model.addAttribute("total", service.count());
        model.addAttribute("done", service.countCompleted());
        model.addAttribute("currentUser", auth != null ? auth.getName() : "");
        return "index";
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task, Authentication auth) {
        service.add(task, auth.getName());
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id,
                             @RequestParam(defaultValue = "false") boolean completed) {
        service.changeStatus(id, completed);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @PostMapping("/addSubTask/{taskId}")
    public String addSubTask(@PathVariable Long taskId,
                             @RequestParam String description) {
        if (description != null && !description.isBlank()) {
            service.addSubTask(taskId, description.trim());
        }
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @PostMapping("/toggleSubTask/{id}")
    public String toggleSubTask(@PathVariable Long id) {
        service.toggleSubTask(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("taskHistory", service.taskHistory());
        model.addAttribute("loginHistory", service.loginHistory());
        model.addAttribute("users", service.allUsers());
        return "history";
    }
}
