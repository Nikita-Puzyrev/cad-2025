package ru.bsuedu.cad.lab5.server;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab4.persistence.DatabaseTaskStore;
import ru.bsuedu.cad.lab4.persistence.DatabaseUsers;
import ru.bsuedu.cad.lab4.persistence.Priority;

@RestController
@RequestMapping("/api")
class TaskApiController {
    private final DatabaseTaskStore tasks;
    private final DatabaseUsers users;

    TaskApiController(DatabaseTaskStore tasks, DatabaseUsers users) {
        this.tasks = tasks;
        this.users = users;
    }

    @GetMapping
    List<DatabaseTaskStore.Summary> list() {
        return tasks.summaries();
    }

    @GetMapping("/{id}")
    DatabaseTaskStore.Details details(@PathVariable long id) {
        return tasks.details(id);
    }

    @GetMapping("/login-history")
    List<DatabaseUsers.Login> logins(Principal principal) {
        return users.logins(principal.getName());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(@RequestBody NewTask input) {
        long id = tasks.create(input.title(), input.description(), input.priority(), input.category());
        return ResponseEntity.created(URI.create("/api/" + id)).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        tasks.delete(id);
    }

    record NewTask(String title, String description, Priority priority, String category) {
    }
}
