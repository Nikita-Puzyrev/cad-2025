package com.fitness.fitness_center.repository;

import com.fitness.fitness_center.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    
    List<Workout> findByWorkoutType(String workoutType);
    
    List<Workout> findByDifficultyLevel(String difficultyLevel);
    
    List<Workout> findByCompleted(boolean completed);
    
    @Query("SELECT COUNT(w) FROM Workout w")
    long countWorkouts();
    
    @Query("SELECT COUNT(w) FROM Workout w WHERE w.completed = true")
    long countCompletedWorkouts();
    
    @Query("SELECT COUNT(w) FROM Workout w WHERE w.completed = false")
    long countPendingWorkouts();
}