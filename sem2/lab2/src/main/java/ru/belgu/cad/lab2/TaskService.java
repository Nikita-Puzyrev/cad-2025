package ru.belgu.cad.lab2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Сервисный слой: бизнес-логика работы с задачами.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public List<Task> getTasks(String category) {
        return taskRepository.getTasksByCategory(category);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }

    public boolean updateStatus(Long id, boolean completed) {
        return taskRepository.updateStatus(id, completed);
    }

    public void addCategory(String name) {
        taskRepository.addCategory(name);
    }

    public Set<String> getCategories() {
        return taskRepository.getCategories();
    }

    public long countByCategory(String category) {
        return taskRepository.countByCategory(category);
    }
}
