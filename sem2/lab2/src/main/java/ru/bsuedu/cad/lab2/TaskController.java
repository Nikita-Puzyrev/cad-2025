package ru.bsuedu.cad.lab2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TaskController {
    private final TaskStore tasks;
    private final boolean showTaskDetails;

    public TaskController(TaskStore tasks, @Value("${tasks.details:false}") boolean showTaskDetails) {
        this.tasks = tasks;
        this.showTaskDetails = showTaskDetails;
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        return showTasks("", model, request);
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String query, Model model, HttpServletRequest request) {
        return showTasks(query, model, request);
    }

    @PostMapping("/addTask")
    public String add(@RequestParam String title, @RequestParam(defaultValue = "") String description) {
        if (title.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите название задачи");
        }
        tasks.add(title.strip(), description.strip());
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    public String update(@PathVariable long id, @RequestParam boolean completed) {
        tasks.update(id, completed);
        return "redirect:/";
    }

    @PostMapping("/deleteTask/{id}")
    public String delete(@PathVariable long id) {
        tasks.delete(id);
        return "redirect:/";
    }

    private String showTasks(String query, Model model, HttpServletRequest request) {
        boolean anonymous = request.getUserPrincipal() == null;
        boolean moderator = request.isUserInRole("MODERATOR");
        model.addAttribute("tasks", tasks.find(query));
        model.addAttribute("query", query);
        model.addAttribute("showTaskDetails", showTaskDetails);
        model.addAttribute("canSearch", anonymous || moderator || request.isUserInRole("SEARCHER"));
        model.addAttribute("canAdd", anonymous || moderator || request.isUserInRole("USER"));
        model.addAttribute("canManage", anonymous || moderator);
        model.addAttribute("signedIn", !anonymous);
        return "tasks";
    }
}
