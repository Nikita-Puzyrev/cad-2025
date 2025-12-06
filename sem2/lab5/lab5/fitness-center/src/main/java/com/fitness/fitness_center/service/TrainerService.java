package com.fitness.fitness_center.service;

import com.fitness.fitness_center.model.Trainer;
import com.fitness.fitness_center.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    
    @Autowired
    private TrainerRepository trainerRepository;
    
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }
    
    public Optional<Trainer> getTrainerById(Integer id) {
        return trainerRepository.findById(id);
    }
    
    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }
    
    public void deleteTrainer(Integer id) {
        trainerRepository.deleteById(id);
    }
    
    public List<Trainer> getAvailableTrainers() {
        return trainerRepository.findAvailableTrainers();
    }
    
    public long getTotalTrainers() {
        return trainerRepository.countTrainers();
    }
}