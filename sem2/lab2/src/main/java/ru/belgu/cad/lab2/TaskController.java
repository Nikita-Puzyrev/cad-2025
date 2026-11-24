package ru.belgu.cad.lab2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер веб-приложения. Возвращает имена шаблонов Thymeleaf,
 * а после изменения данных перенаправляет на главную страницу.
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

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        if (task.getTitle() != null && !task.getTitle().isBlank()) {
            taskService.addTask(task);
        }
        return "redirect:/";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name) {
        taskService.addCategory(name);
        return "redirect:/";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id, @RequestParam boolean completed) {
        taskService.updateStatus(id, completed);
        return "redirect:/";
    }
}
