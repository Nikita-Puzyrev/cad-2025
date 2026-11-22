package ru.bsuedu.cad.lab4.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
class StoredTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 120)
    String title;

    @Column(nullable = false, columnDefinition = "text")
    String description;

    boolean completed;

    @Column(nullable = false)
    Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Priority priority;

    @Column(nullable = false, length = 80)
    String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    UserAccount owner;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    List<SubTask> subtasks = new ArrayList<>();

    @Version
    long version;

    protected StoredTask() {
    }

    StoredTask(String title, String description, Priority priority, String category, UserAccount owner) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.owner = owner;
    }
}
