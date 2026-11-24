package ru.bsu.lab3;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public void addTask(String title, String description) {
        if (title.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Укажите название задачи");
        }
        repository.add(title.trim(), description.trim());
    }

    public void updateTask(long id, boolean completed) {
        Task task = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));
        task.setCompleted(completed);
    }

    public void deleteTask(long id) {
        if (!repository.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена");
        }
    }

    public List<Task> search(String query) {
        String text = query.trim().toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(task -> task.getTitle().toLowerCase(Locale.ROOT).contains(text)
                        || task.getDescription().toLowerCase(Locale.ROOT).contains(text))
                .toList();
    }
}
