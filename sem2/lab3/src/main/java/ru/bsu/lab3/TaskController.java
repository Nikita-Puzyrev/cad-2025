package ru.bsu.lab3;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @ModelAttribute
    public void permissions(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("canModerate", authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MODERATOR")));
        model.addAttribute("canSearch", authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SEARCHER")));
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", service.getAllTasks());
        model.addAttribute("query", "");
        return "index";
    }

    @PreAuthorize("hasRole('SEARCHER')")
    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("tasks", service.search(q));
        model.addAttribute("query", q);
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String title, @RequestParam(defaultValue = "") String description) {
        service.addTask(title, description);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable long id, @RequestParam boolean completed) {
        service.updateTask(id, completed);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable long id) {
        service.deleteTask(id);
        return "redirect:/";
    }
}
