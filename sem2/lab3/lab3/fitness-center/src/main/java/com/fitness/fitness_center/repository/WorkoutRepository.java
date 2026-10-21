package com.fitness.fitness_center.repository;

import com.fitness.fitness_center.model.Workout;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WorkoutRepository {
    
    private List<Workout> workoutList = new ArrayList<>();
    
    // Инициализация тестовыми данными
    public WorkoutRepository() {
        workoutList.add(new Workout("Утренняя пробежка", "Бег на 5 км в парке", "Кардио", 30, "Легкий"));
        workoutList.add(new Workout("Силовая тренировка", "Упражнения с гантелями", "Силовая", 60, "Средний"));
        workoutList.add(new Workout("Йога для начинающих", "Базовые асаны и дыхательные упражнения", "Йога", 45, "Легкий"));
    }
    
    // Метод для добавления тренировки
    public void addWorkout(Workout workout) {
        workoutList.add(workout);
    }
    
    // Метод для получения всех тренировок
    public List<Workout> getAllWorkouts() {
        return new ArrayList<>(workoutList);
    }
    
    // Метод для получения тренировки по ID
    public Optional<Workout> getWorkoutById(Long id) {
        return workoutList.stream()
                .filter(workout -> workout.getId().equals(id))
                .findFirst();
    }
    
    // Метод для удаления тренировки по ID
    public boolean deleteWorkout(Long id) {
        return workoutList.removeIf(workout -> workout.getId().equals(id));
    }
    
    // Метод для обновления тренировки
    public void updateWorkout(Workout updatedWorkout) {
        for (int i = 0; i < workoutList.size(); i++) {
            if (workoutList.get(i).getId().equals(updatedWorkout.getId())) {
                workoutList.set(i, updatedWorkout);
                break;
            }
        }
    }
    
    // Метод для поиска тренировок по типу
    public List<Workout> findWorkoutsByType(String type) {
        return workoutList.stream()
                .filter(workout -> workout.getWorkoutType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
    
    // Метод для поиска тренировок по уровню сложности
    public List<Workout> findWorkoutsByDifficulty(String difficulty) {
        return workoutList.stream()
                .filter(workout -> workout.getDifficultyLevel().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
    }
    
    // Метод для получения завершенных тренировок
    public List<Workout> getCompletedWorkouts() {
        return workoutList.stream()
                .filter(Workout::isCompleted)
                .collect(Collectors.toList());
    }
    
    // Метод для получения незавершенных тренировок
    public List<Workout> getPendingWorkouts() {
        return workoutList.stream()
                .filter(workout -> !workout.isCompleted())
                .collect(Collectors.toList());
    }
    
    // Метод для подсчета тренировок
    public int getWorkoutCount() {
        return workoutList.size();
    }
    
    // Метод для очистки всех тренировок
    public void clearAllWorkouts() {
        workoutList.clear();
    }
}