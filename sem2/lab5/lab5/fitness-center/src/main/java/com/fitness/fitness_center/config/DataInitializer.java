package com.fitness.fitness_center.config;

import com.fitness.fitness_center.model.Member;
import com.fitness.fitness_center.model.Trainer;
import com.fitness.fitness_center.model.Workout;
import com.fitness.fitness_center.repository.MemberRepository;
import com.fitness.fitness_center.repository.TrainerRepository;
import com.fitness.fitness_center.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private TrainerRepository trainerRepository;
    
    @Autowired
    private WorkoutRepository workoutRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Инициализация тренеров
        if (trainerRepository.count() == 0) {
            Trainer trainer1 = new Trainer(
                "Александр Иванов", 
                "Силовые тренировки", 
                5, 
                "+79161112233", 
                "alex@fitness.com", 
                "Пн-Пт: 9:00-18:00", 
                2000.0, 
                10
            );
            
            Trainer trainer2 = new Trainer(
                "Екатерина Петрова", 
                "Йога и растяжка", 
                3, 
                "+79162223344", 
                "ekaterina@fitness.com", 
                "Вт-Сб: 10:00-19:00", 
                1500.0, 
                8
            );
            
            trainerRepository.save(trainer1);
            trainerRepository.save(trainer2);
            
            System.out.println("Добавлены тестовые тренеры");
        }
        
        // Инициализация членов
        if (memberRepository.count() == 0) {
            Member member1 = new Member(
                "Иван Петров", 
                "Gold", 
                28, 
                "+79161234567", 
                "ivan@email.com", 
                1
            );
            
            Member member2 = new Member(
                "Анна Сидорова", 
                "Silver", 
                25, 
                "+79169876543", 
                "anna@email.com", 
                1
            );
            
            memberRepository.save(member1);
            memberRepository.save(member2);
            
            System.out.println("Добавлены тестовые члены");
        }
        
        // Инициализация тренировок
        if (workoutRepository.count() == 0) {
            Workout workout1 = new Workout(
                "Утренняя пробежка", 
                "Бег на 5 км в парке", 
                "Кардио", 
                30, 
                "Легкий"
            );
            
            Workout workout2 = new Workout(
                "Силовая тренировка", 
                "Упражнения с гантелями", 
                "Силовая", 
                60, 
                "Средний"
            );
            
            workoutRepository.save(workout1);
            workoutRepository.save(workout2);
            
            System.out.println("Добавлены тестовые тренировки");
        }
        
        System.out.println("Инициализация данных завершена");
    }
}