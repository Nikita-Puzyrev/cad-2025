package ru.belgu.cad.lab2;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Слой доступа к данным: задачи и категории хранятся в памяти.
 */
@Repository
public class TaskRepository {

    private final List<Task> taskList = new ArrayList<>();
    private final Set<String> categories = new LinkedHashSet<>(
            List.of("Учеба", "Личные дела", "Работа"));

    public void addTask(Task task) {
        taskList.add(task);
    }

    public List<Task> getAllTasks() {
        return List.copyOf(taskList);
    }

    public List<Task> getTasksByCategory(String category) {
        if (category == null || category.isBlank()) {
            return getAllTasks();
        }
        return taskList.stream()
                .filter(t -> category.equals(t.getCategory()))
                .toList();
    }

    public void deleteTask(Long id) {
        taskList.removeIf(task -> task.getId().equals(id));
    }

    public boolean updateStatus(Long id, boolean completed) {
        for (Task task : taskList) {
            if (task.getId().equals(id)) {
                task.setCompleted(completed);
                return true;
            }
        }
        return false;
    }

    public void addCategory(String name) {
        if (name != null && !name.isBlank()) {
            categories.add(name.trim());
        }
    }

    public Set<String> getCategories() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(categories));
    }

    public long countByCategory(String category) {
        return taskList.stream().filter(t -> category.equals(t.getCategory())).count();
    }
}
