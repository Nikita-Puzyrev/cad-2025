package com.example.demo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    private List<Task> taskList = new ArrayList<>();

    public void addTask(Task task) {
        taskList.add(task);
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public void deleteTask(Long id) {
        taskList.removeIf(task -> task.getId().equals(id));
    }
}