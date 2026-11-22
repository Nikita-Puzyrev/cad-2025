package ru.bsuedu.cad.lab4.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.bsuedu.cad.lab2.Task;
import ru.bsuedu.cad.lab2.TaskStore;

@Transactional(readOnly = true)
public class DatabaseTaskStore implements TaskStore {
    private final StoredTaskRepository tasks;
    private final TaskHistoryRepository history;
    private final DatabaseUsers users;
    private final ObjectMapper json;

    DatabaseTaskStore(StoredTaskRepository tasks, TaskHistoryRepository history, DatabaseUsers users, ObjectMapper json) {
        this.tasks = tasks;
        this.history = history;
        this.users = users;
        this.json = json;
    }

    @Override
    public List<Task> find(String query) {
        String term = query.strip().toLowerCase(Locale.ROOT);
        return tasks.findAllByOrderByIdAsc().stream()
                .map(task -> new Task(task.id, task.title, task.description, task.completed))
                .filter(task -> task.matches(term))
                .toList();
    }

    public List<Summary> summaries() {
        return tasks.findAllByOrderByIdAsc().stream().map(this::summary).toList();
    }

    public Details details(long id) {
        StoredTask task = task(id);
        var subtasks = task.subtasks.stream()
                .map(sub -> new Subtask(sub.id, sub.description, sub.completed))
                .toList();
        var changes = history.findByTaskIdOrderByIdDesc(id).stream()
                .map(change -> new Change(change.changedAt, change.oldState, change.newState))
                .toList();
        return new Details(summary(task), subtasks, changes);
    }

    @Override
    @Transactional
    public void add(String title, String description) {
        create(title, description, Priority.NORMAL, "");
    }

    @Transactional
    public long create(String title, String description, Priority priority, String category) {
        if (title == null || title.isBlank() || title.strip().length() > 120) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название должно содержать от 1 до 120 символов");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var task = new StoredTask(title.strip(), description == null ? "" : description.strip(),
                priority == null ? Priority.NORMAL : priority, cleanCategory(category), users.account(username));
        tasks.save(task);
        history.save(new TaskHistory(task.id, null, snapshot(task)));
        return task.id;
    }

    @Override
    @Transactional
    public void update(long id, boolean completed) {
        StoredTask task = task(id);
        if (task.completed != completed) {
            String oldState = snapshot(task);
            task.completed = completed;
            history.save(new TaskHistory(id, oldState, snapshot(task)));
        }
    }

    @Transactional
    public void settings(long id, Priority priority, String category) {
        StoredTask task = task(id);
        String cleanCategory = cleanCategory(category);
        if (task.priority != priority || !task.category.equals(cleanCategory)) {
            String oldState = snapshot(task);
            task.priority = priority;
            task.category = cleanCategory;
            history.save(new TaskHistory(id, oldState, snapshot(task)));
        }
    }

    @Transactional
    public void addSubtask(long id, String description) {
        StoredTask task = task(id);
        if (description.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите описание подзадачи");
        }
        task.subtasks.add(new SubTask(task, description.strip()));
    }

    @Transactional
    public void completeSubtask(long id, long subtaskId, boolean completed) {
        StoredTask task = task(id);
        SubTask subtask = task.subtasks.stream().filter(sub -> sub.id == subtaskId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subtask.completed = completed;
    }

    @Override
    @Transactional
    public void delete(long id) {
        StoredTask task = task(id);
        history.save(new TaskHistory(id, snapshot(task), null));
        tasks.delete(task);
    }

    private StoredTask task(long id) {
        return tasks.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private String cleanCategory(String category) {
        String result = category == null ? "" : category.strip();
        if (result.length() > 80) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Категория может содержать до 80 символов");
        }
        return result;
    }

    private Summary summary(StoredTask task) {
        return new Summary(task.id, task.title, task.description, task.completed, task.createdAt,
                task.priority, task.category, task.owner.username());
    }

    private String snapshot(StoredTask task) {
        try {
            return json.writeValueAsString(summary(task));
        } catch (JsonProcessingException error) {
            throw new IllegalStateException(error);
        }
    }

    public record Summary(long id, String title, String description, boolean completed, Instant createdAt,
                          Priority priority, String category, String owner) {
    }

    public record Details(Summary task, List<Subtask> subtasks, List<Change> changes) {
    }

    public record Subtask(long id, String description, boolean completed) {
    }

    public record Change(Instant changedAt, String oldState, String newState) {
    }
}
