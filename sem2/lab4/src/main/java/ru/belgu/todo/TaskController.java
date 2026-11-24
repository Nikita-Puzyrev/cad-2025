package ru.belgu.todo;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TaskController {
    private final TaskRepository tasks;

    public TaskController(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("tasks", tasks.findAll(Sort.by("id")));
        model.addAttribute("moderator", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR")));
        return "index";
    }

    @PostMapping("/addTask")
    public String add(@RequestParam String description, @RequestParam(defaultValue = "2") int priority) {
        if (description.isBlank() || description.length() > 500 || priority < 1 || priority > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Проверьте описание и приоритет");
        }
        tasks.save(new Task(description, priority));
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String update(@PathVariable Long id, @RequestParam boolean completed) {
        Task task = findTask(id);
        task.setCompleted(completed);
        tasks.save(task);
        return "redirect:/";
    }

    @PostMapping("/deleteTask/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String delete(@PathVariable Long id) {
        tasks.delete(findTask(id));
        return "redirect:/";
    }

    private Task findTask(Long id) {
        return tasks.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));
    }
}
