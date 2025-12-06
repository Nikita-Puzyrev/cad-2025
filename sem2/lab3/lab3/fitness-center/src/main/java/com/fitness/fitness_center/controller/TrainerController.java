package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Trainer;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    
    private List<Trainer> trainers = new ArrayList<>();
    private int nextId = 1;
    
    // Инициализация тестовыми данными
    public TrainerController() {
        trainers.add(new Trainer(nextId++, "Александр Иванов", "Силовые тренировки", 
                5, "+79161112233", "alex@fitness.com", 
                "Пн-Пт: 9:00-18:00", 2000.0, 10));
        
        trainers.add(new Trainer(nextId++, "Екатерина Петрова", "Йога и растяжка", 
                3, "+79162223344", "ekaterina@fitness.com", 
                "Вт-Сб: 10:00-19:00", 1500.0, 8));
        
        trainers.add(new Trainer(nextId++, "Дмитрий Сидоров", "Кардио и функциональный тренинг", 
                7, "+79163334455", "dmitry@fitness.com", 
                "Ср-Вс: 8:00-17:00", 1800.0, 12));
    }
    
    // Метод для получения списка тренеров (для MemberController)
    public List<Trainer> getTrainersList() {
        return trainers;
    }
    
    // 1. Получить всех тренеров
    @GetMapping
    public List<Trainer> getAllTrainers() {
        return trainers;
    }
    
    // 2. Получить тренера по ID
    @GetMapping("/{id}")
    public Trainer getTrainerById(@PathVariable int id) {
        return trainers.stream()
                .filter(trainer -> trainer.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    // 3. Добавить нового тренера
    @PostMapping
    public String addTrainer(@RequestBody Trainer newTrainer) {
        // Валидация данных
        if (newTrainer.getName() == null || newTrainer.getName().trim().isEmpty()) {
            return "Ошибка: Имя тренера не может быть пустым";
        }
        
        if (newTrainer.getExperienceYears() < 0) {
            return "Ошибка: Опыт работы не может быть отрицательным";
        }
        
        if (newTrainer.getHourlyRate() <= 0) {
            return "Ошибка: Стоимость часа должна быть положительной";
        }
        
        if (newTrainer.getMaxClients() <= 0) {
            return "Ошибка: Максимальное количество клиентов должно быть положительным";
        }
        
        newTrainer.setId(nextId++);
        newTrainer.setCurrentClients(0); // Начинаем с 0 клиентов
        trainers.add(newTrainer);
        return "Тренер успешно добавлен! ID: " + newTrainer.getId();
    }
    
    // 4. Обновить информацию о тренере
    @PutMapping("/{id}")
    public String updateTrainer(@PathVariable int id, @RequestBody Trainer updatedTrainer) {
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getId() == id) {
                // Сохраняем текущее количество клиентов
                int currentClients = trainers.get(i).getCurrentClients();
                
                updatedTrainer.setId(id);
                updatedTrainer.setCurrentClients(currentClients);
                
                // Проверяем, что новое максимальное количество не меньше текущего
                if (updatedTrainer.getMaxClients() < currentClients) {
                    return "Ошибка: Новое максимальное количество клиентов (" + 
                           updatedTrainer.getMaxClients() + ") меньше текущего количества (" + 
                           currentClients + ")";
                }
                
                trainers.set(i, updatedTrainer);
                return "Информация о тренере успешно обновлена!";
            }
        }
        return "Ошибка: Тренер с ID " + id + " не найден";
    }
    
    // 5. Удалить тренера
    @DeleteMapping("/{id}")
    public String deleteTrainer(@PathVariable int id) {
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getId() == id) {
                // Проверяем, есть ли у тренера клиенты
                if (trainers.get(i).getCurrentClients() > 0) {
                    return "Ошибка: Нельзя удалить тренера, у которого есть клиенты. " +
                           "Текущее количество клиентов: " + trainers.get(i).getCurrentClients();
                }
                
                trainers.remove(i);
                return "Тренер успешно удален!";
            }
        }
        return "Ошибка: Тренер с ID " + id + " не найден";
    }
    
    // 6. Получить количество тренеров
    @GetMapping("/count")
    public int getTrainersCount() {
        return trainers.size();
    }
    
    // 7. Поиск тренеров по специализации
    @GetMapping("/specialization/{specialization}")
    public List<Trainer> getTrainersBySpecialization(@PathVariable String specialization) {
        return trainers.stream()
                .filter(trainer -> trainer.getSpecialization().toLowerCase()
                        .contains(specialization.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // 8. Получить тренеров с доступными местами
    @GetMapping("/available")
    public List<Trainer> getAvailableTrainers() {
        return trainers.stream()
                .filter(Trainer::hasAvailableSlots)
                .collect(Collectors.toList());
    }
    
    // 9. Поиск тренеров по опыту работы
    @GetMapping("/experience")
    public List<Trainer> getTrainersByExperience(
            @RequestParam(defaultValue = "0") int minYears,
            @RequestParam(required = false) Integer maxYears) {
        
        return trainers.stream()
                .filter(trainer -> {
                    if (maxYears != null) {
                        return trainer.getExperienceYears() >= minYears && 
                               trainer.getExperienceYears() <= maxYears;
                    }
                    return trainer.getExperienceYears() >= minYears;
                })
                .collect(Collectors.toList());
    }
    
    // 10. Получить статистику по тренерам
    @GetMapping("/stats")
    public String getTrainersStats() {
        int totalTrainers = trainers.size();
        int totalClients = trainers.stream().mapToInt(Trainer::getCurrentClients).sum();
        int totalCapacity = trainers.stream().mapToInt(Trainer::getMaxClients).sum();
        int availableSlots = totalCapacity - totalClients;
        double avgExperience = trainers.stream()
                .mapToInt(Trainer::getExperienceYears)
                .average()
                .orElse(0.0);
        double avgRate = trainers.stream()
                .mapToDouble(Trainer::getHourlyRate)
                .average()
                .orElse(0.0);
        
        return String.format("""
                Статистика тренеров:
                Всего тренеров: %d
                Всего клиентов: %d
                Общая вместимость: %d
                Свободных мест: %d
                Средний опыт работы: %.1f лет
                Средняя стоимость часа: %.2f руб.
                """, totalTrainers, totalClients, totalCapacity, availableSlots, 
                avgExperience, avgRate);
    }
    
    // 11. Получить тренеров по стоимости
    @GetMapping("/price-range")
    public List<Trainer> getTrainersByPriceRange(
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        
        return trainers.stream()
                .filter(trainer -> {
                    if (maxPrice != null) {
                        return trainer.getHourlyRate() >= minPrice && 
                               trainer.getHourlyRate() <= maxPrice;
                    }
                    return trainer.getHourlyRate() >= minPrice;
                })
                .collect(Collectors.toList());
    }
}