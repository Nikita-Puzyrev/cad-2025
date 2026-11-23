package ru.belgu.cad.lab3;

import java.time.LocalDateTime;

/**
 * Модель задачи. Дополнительное задание (вариант 1) — категория задачи.
 */
public class Task {

    private static Long nextId = 1L;

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private String category;
    private LocalDateTime createdAt;

    public Task() {
        this.id = generateId();
        this.createdAt = LocalDateTime.now();
    }

    public Task(String title, String description, String category) {
        this();
        this.title = title;
        this.description = description;
        this.category = category;
        this.completed = false;
    }

    private static synchronized Long generateId() {
        return nextId++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', description='" + description
                + "', completed=" + completed + ", category='" + category + "'}";
    }
}
