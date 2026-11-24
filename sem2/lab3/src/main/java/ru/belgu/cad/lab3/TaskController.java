package ru.belgu.cad.lab3;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер веб-приложения с разграничением прав по ролям.
 *
 * Добавление задач — USER, MODERATOR, ADMIN;
 * изменение статуса и удаление — MODERATOR, ADMIN;
 * управление категориями — только ADMIN;
 * роль GUEST имеет доступ только к просмотру.
 */
@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("tasks", taskService.getTasks(category));
        model.addAttribute("categories", taskService.getCategories());
        model.addAttribute("selectedCategory", category == null ? "" : category);
        model.addAttribute("total", taskService.getAllTasks().size());
        return "index";
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        if (task.getTitle() != null && !task.getTitle().isBlank()) {
            taskService.addTask(task);
        }
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name) {
        taskService.addCategory(name);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id, @RequestParam boolean completed) {
        taskService.updateStatus(id, completed);
        return "redirect:/";
    }
}
