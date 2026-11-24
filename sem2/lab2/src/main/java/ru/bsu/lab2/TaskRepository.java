package ru.bsu.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public synchronized Task add(String title, String description) {
        Task task = new Task(nextId++, title, description);
        tasks.add(task);
        return task;
    }

    public synchronized List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public synchronized Optional<Task> findById(long id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    public synchronized boolean delete(long id) {
        return tasks.removeIf(task -> task.getId() == id);
    }
}
