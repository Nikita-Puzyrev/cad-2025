package ru.bsuedu.cad.lab4.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_tasks")
class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, columnDefinition = "text")
    String description;

    boolean completed;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    StoredTask task;

    protected SubTask() {
    }

    SubTask(StoredTask task, String description) {
        this.task = task;
        this.description = description;
    }
}
