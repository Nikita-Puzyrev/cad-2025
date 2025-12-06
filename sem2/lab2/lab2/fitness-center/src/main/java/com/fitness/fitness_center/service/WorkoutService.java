package com.fitness.fitness_center.service;

import com.fitness.fitness_center.model.Workout;
import com.fitness.fitness_center.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {
    
    private final WorkoutRepository workoutRepository;
    
    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }
    
    // Метод для добавления новой тренировки
    public void addWorkout(Workout workout) {
        workoutRepository.addWorkout(workout);
    }
    
    // Метод для получения всех тренировок
    public List<Workout> getAllWorkouts() {
        return workoutRepository.getAllWorkouts();
    }
    
    // Метод для получения тренировки по ID
    public Optional<Workout> getWorkoutById(Long id) {
        return workoutRepository.getWorkoutById(id);
    }
    
    // Метод для удаления тренировки по ID
    public boolean deleteWorkout(Long id) {
        return workoutRepository.deleteWorkout(id);
    }
    
    // Метод для обновления тренировки
    public void updateWorkout(Workout workout) {
        workoutRepository.updateWorkout(workout);
    }
    
    // Метод для поиска тренировок по типу
    public List<Workout> findWorkoutsByType(String type) {
        return workoutRepository.findWorkoutsByType(type);
    }
    
    // Метод для поиска тренировок по уровню сложности
    public List<Workout> findWorkoutsByDifficulty(String difficulty) {
        return workoutRepository.findWorkoutsByDifficulty(difficulty);
    }
    
    // Метод для получения завершенных тренировок
    public List<Workout> getCompletedWorkouts() {
        return workoutRepository.getCompletedWorkouts();
    }
    
    // Метод для получения незавершенных тренировок
    public List<Workout> getPendingWorkouts() {
        return workoutRepository.getPendingWorkouts();
    }
    
    // Метод для пометки тренировки как выполненной
    public boolean markWorkoutAsCompleted(Long id) {
        Optional<Workout> workoutOpt = workoutRepository.getWorkoutById(id);
        if (workoutOpt.isPresent()) {
            Workout workout = workoutOpt.get();
            workout.setCompleted(true);
            workoutRepository.updateWorkout(workout);
            return true;
        }
        return false;
    }
    
    // Метод для пометки тренировки как незавершенной
    public boolean markWorkoutAsPending(Long id) {
        Optional<Workout> workoutOpt = workoutRepository.getWorkoutById(id);
        if (workoutOpt.isPresent()) {
            Workout workout = workoutOpt.get();
            workout.setCompleted(false);
            workoutRepository.updateWorkout(workout);
            return true;
        }
        return false;
    }
    
    // Метод для подсчета тренировок
    public int getWorkoutCount() {
        return workoutRepository.getWorkoutCount();
    }
    
    // Метод для получения статистики
    public String getWorkoutStats() {
        int total = getWorkoutCount();
        int completed = getCompletedWorkouts().size();
        int pending = getPendingWorkouts().size();
        
        return String.format("""
                Статистика тренировок:
                Всего тренировок: %d
                Выполнено: %d (%.1f%%)
                Ожидают выполнения: %d (%.1f%%)
                """, total, completed, 
                total > 0 ? (completed * 100.0 / total) : 0,
                pending,
                total > 0 ? (pending * 100.0 / total) : 0);
    }
    
    // Метод для очистки всех тренировок
    public void clearAllWorkouts() {
        workoutRepository.clearAllWorkouts();
    }
}