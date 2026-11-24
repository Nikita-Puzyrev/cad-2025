package ru.belgu.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 500)
    private String description;
    @Column(nullable = false)
    private boolean completed;
    @Column(nullable = false)
    private int priority;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Task() {
    }

    public Task(String description, int priority) {
        this.description = description.trim();
        this.priority = priority;
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public int getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
