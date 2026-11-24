package ru.bsu.lab2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", service.getAllTasks());
        model.addAttribute("query", "");
        return "index";
    }

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

    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable long id, @RequestParam boolean completed) {
        service.updateTask(id, completed);
        return "redirect:/";
    }

    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable long id) {
        service.deleteTask(id);
        return "redirect:/";
    }
}
