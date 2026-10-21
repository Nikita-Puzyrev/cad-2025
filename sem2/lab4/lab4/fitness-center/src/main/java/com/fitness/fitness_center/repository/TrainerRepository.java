package com.fitness.fitness_center.repository;

import com.fitness.fitness_center.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    
    List<Trainer> findBySpecializationContainingIgnoreCase(String specialization);
    
    @Query("SELECT t FROM Trainer t WHERE t.currentClients < t.maxClients")
    List<Trainer> findAvailableTrainers();
    
    @Query("SELECT COUNT(t) FROM Trainer t")
    long countTrainers();
}