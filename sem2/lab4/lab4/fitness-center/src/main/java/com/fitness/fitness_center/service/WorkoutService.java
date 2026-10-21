package com.fitness.fitness_center.service;

import com.fitness.fitness_center.model.Workout;
import com.fitness.fitness_center.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {
    
    @Autowired
    private WorkoutRepository workoutRepository;
    
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }
    
    public Optional<Workout> getWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }
    
    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }
    
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
    
    public List<Workout> getWorkoutsByType(String type) {
        return workoutRepository.findByWorkoutType(type);
    }
    
    public List<Workout> getWorkoutsByDifficulty(String difficulty) {
        return workoutRepository.findByDifficultyLevel(difficulty);
    }
    
    public List<Workout> getCompletedWorkouts() {
        return workoutRepository.findByCompleted(true);
    }
    
    public List<Workout> getPendingWorkouts() {
        return workoutRepository.findByCompleted(false);
    }
    
    public long getTotalWorkouts() {
        return workoutRepository.countWorkouts();
    }
    
    public long getCompletedWorkoutsCount() {
        return workoutRepository.countCompletedWorkouts();
    }
    
    public long getPendingWorkoutsCount() {
        return workoutRepository.countPendingWorkouts();
    }
    
    public String getWorkoutStats() {
        long total = getTotalWorkouts();
        long completed = getCompletedWorkoutsCount();
        long pending = getPendingWorkoutsCount();
        
        if (total == 0) {
            return "Статистика тренировок:\nВсего тренировок: 0";
        }
        
        double completedPercent = (completed * 100.0) / total;
        double pendingPercent = (pending * 100.0) / total;
        
        return String.format("""
                Статистика тренировок:
                Всего тренировок: %d
                Выполнено: %d (%.1f%%)
                Ожидают выполнения: %d (%.1f%%)
                """, total, completed, completedPercent, pending, pendingPercent);
    }
}