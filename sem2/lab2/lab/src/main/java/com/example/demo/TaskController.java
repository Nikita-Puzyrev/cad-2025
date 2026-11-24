package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    private List<Task> taskList = new ArrayList<>();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskList);
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String title, @RequestParam String description) {
        Task task = new Task(title, description);
        taskList.add(task);
        return "redirect:/";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskList.removeIf(task -> task.getId().equals(id));
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id) {
        taskList.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .ifPresent(task -> task.setCompleted(true));
        return "redirect:/";
    }
}