package com.example.androidapp;
public class Task {
    private Long id;
    private String description;
    private boolean completed;
    private String category;
    private String priority;

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
}