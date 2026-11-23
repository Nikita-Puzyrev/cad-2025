package ru.belgu.cad.lab5.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.belgu.cad.lab5.domain.Priority;
import ru.belgu.cad.lab5.domain.Task;
import ru.belgu.cad.lab5.dto.NewTaskRequest;
import ru.belgu.cad.lab5.dto.TaskDto;
import ru.belgu.cad.lab5.dto.UserInfo;
import ru.belgu.cad.lab5.repository.TaskRepository;
import ru.belgu.cad.lab5.service.TaskService;

import java.util.Comparator;
import java.util.List;

/**
 * REST-интерфейс для клиентского приложения (Swing).
 * Аутентификация — HTTP Basic по данным таблицы app_users.
 */
@RestController
@RequestMapping("/api")
public class TaskRestController {

    private final TaskRepository tasks;
    private final TaskService service;

    public TaskRestController(TaskRepository tasks, TaskService service) {
        this.tasks = tasks;
        this.service = service;
    }

    @GetMapping("/me")
    public UserInfo me(Authentication auth) {
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new UserInfo(auth.getName(), roles);
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'MODERATOR', 'ADMIN')")
    public List<TaskDto> list(
            @RequestParam(required = false, defaultValue = "created") String sort,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        List<Task> all = tasks.findAllWithSubTasks();
        Comparator<Task> comparator = switch (sort) {
            case "priority" -> Comparator.comparing(Task::getPriority);
            case "category" -> Comparator.comparing(t -> t.getCategory() == null
                    ? "" : t.getCategory());
            case "title" -> Comparator.comparing(Task::getTitle);
            case "status" -> Comparator.comparing(Task::isCompleted);
            default -> Comparator.comparing(Task::getCreatedAt);
        };
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        return all.stream().sorted(comparator).map(TaskRestController::toDto).toList();
    }

    @GetMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<TaskDto> one(@PathVariable Long id) {
        return tasks.findById(id)
                .map(t -> ResponseEntity.ok(toDto(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<TaskDto> create(@RequestBody NewTaskRequest request,
                                          Authentication auth) {
        Task task = new Task(
                request.title(),
                request.description() == null ? "" : request.description(),
                request.category(),
                request.priority() == null || request.priority().isBlank()
                        ? Priority.NORMAL
                        : Priority.valueOf(request.priority()));
        Task saved = service.add(task, auth.getName());
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (tasks.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static TaskDto toDto(Task task) {
        List<TaskDto.SubTaskDto> subTasks = task.getSubTasks().stream()
                .map(s -> new TaskDto.SubTaskDto(s.getId(), s.getDescription(),
                        s.isCompleted()))
                .toList();
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getPriority().name(),
                task.getCategory(),
                task.getCreatedAt() == null ? "" : task.getCreatedAt().format(TaskDto.FORMAT),
                task.getAuthor() == null ? "—" : task.getAuthor().getUsername(),
                subTasks);
    }
}
