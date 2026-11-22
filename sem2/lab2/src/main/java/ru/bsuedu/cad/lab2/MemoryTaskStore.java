package ru.bsuedu.cad.lab2;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class MemoryTaskStore implements TaskStore {
    private final Map<Long, Task> tasks = new LinkedHashMap<>();
    private long nextId = 1;

    @Override
    public synchronized List<Task> find(String query) {
        String term = query.strip().toLowerCase(Locale.ROOT);
        return tasks.values().stream()
                .filter(task -> task.matches(term))
                .toList();
    }

    @Override
    public synchronized void add(String title, String description) {
        long id = nextId++;
        tasks.put(id, new Task(id, title, description, false));
    }

    @Override
    public synchronized void update(long id, boolean completed) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        tasks.put(id, new Task(id, task.title(), task.description(), completed));
    }

    @Override
    public synchronized void delete(long id) {
        if (tasks.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
