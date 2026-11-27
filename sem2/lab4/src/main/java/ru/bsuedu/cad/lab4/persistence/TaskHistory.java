package ru.bsuedu.cad.lab4.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "task_history")
class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    long taskId;

    @Column(nullable = false)
    Instant changedAt = Instant.now();

    @Column(columnDefinition = "text")
    String oldState;

    @Column(columnDefinition = "text")
    String newState;

    protected TaskHistory() {
    }

    TaskHistory(long taskId, String oldState, String newState) {
        this.taskId = taskId;
        this.oldState = oldState;
        this.newState = newState;
    }
}
