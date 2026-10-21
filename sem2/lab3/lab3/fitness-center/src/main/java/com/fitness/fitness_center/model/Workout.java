package com.fitness.fitness_center.model;

public class Workout {
    private static Long nextId = 1L; // Статическая переменная для генерации ID
    private Long id;
    private String title;
    private String description;
    private String workoutType; // Тип тренировки: кардио, силовая, йога и т.д.
    private Integer durationMinutes; // Продолжительность в минутах
    private String difficultyLevel; // Уровень сложности: легкий, средний, тяжелый
    private boolean completed; // Статус выполнения
    
    public Workout() {
        this.id = generateId();
    }
    
    public Workout(String title, String description, String workoutType, 
                   Integer durationMinutes, String difficultyLevel) {
        this.id = generateId();
        this.title = title;
        this.description = description;
        this.workoutType = workoutType;
        this.durationMinutes = durationMinutes;
        this.difficultyLevel = difficultyLevel;
        this.completed = false;
    }
    
    private synchronized Long generateId() {
        return nextId++;
    }
    
    // Геттеры и сеттеры
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
    
    public String getWorkoutType() {
        return workoutType;
    }
    
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", workoutType='" + workoutType + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", completed=" + completed +
                '}';
    }
}