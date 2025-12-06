package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Member;
import com.fitness.fitness_center.model.Trainer;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    private List<Member> members = new ArrayList<>();
    private List<Trainer> trainers; // Будет получен из TrainerController
    
    private int nextId = 1;
    
    // Инициализация тестовыми данными
    public MemberController(TrainerController trainerController) {
        // Получаем тренеров из TrainerController
        this.trainers = trainerController.getTrainersList();
        
        members.add(new Member(nextId++, "Иван Петров", "Gold", 28, 
                "+79161234567", "ivan@email.com", 1));
        members.add(new Member(nextId++, "Анна Сидорова", "Silver", 25, 
                "+79169876543", "anna@email.com", 1));
        members.add(new Member(nextId++, "Алексей Смирнов", "Premium", 35, 
                "+79165554433", "alex@email.com", 2));
    }
    
    // 1. Получить всех членов фитнес-центра
    @GetMapping
    public List<Member> getAllMembers() {
        return members;
    }
    
    // 2. Получить члена по ID
    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable int id) {
        return members.stream()
                .filter(member -> member.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    // 3. Добавить нового члена
    @PostMapping
    public String addMember(@RequestBody Member newMember) {
        // Проверка на пустые поля
        if (newMember.getName() == null || newMember.getName().trim().isEmpty()) {
            return "Ошибка: Имя не может быть пустым";
        }
        
        if (newMember.getAge() <= 0 || newMember.getAge() > 100) {
            return "Ошибка: Возраст должен быть от 1 до 100 лет";
        }
        
        // Проверка типа членства
        if (!isValidMembershipType(newMember.getMembershipType())) {
            return "Ошибка: Неверный тип членства. Доступные типы: Basic, Silver, Gold, Premium";
        }
        
        // Проверка тренера, если указан
        if (newMember.getTrainerId() != null) {
            Trainer trainer = trainers.stream()
                    .filter(t -> t.getId() == newMember.getTrainerId())
                    .findFirst()
                    .orElse(null);
            
            if (trainer == null) {
                return "Ошибка: Тренер с ID " + newMember.getTrainerId() + " не найден";
            }
            
            if (!trainer.hasAvailableSlots()) {
                return "Ошибка: У тренера " + trainer.getName() + " нет свободных мест";
            }
            
            // Увеличиваем счетчик клиентов у тренера
            trainer.incrementClients();
        }
        
        newMember.setId(nextId++);
        members.add(newMember);
        return "Член фитнес-центра успешно добавлен! ID: " + newMember.getId();
    }
    
    // 4. Обновить информацию о члене
    @PutMapping("/{id}")
    public String updateMember(@PathVariable int id, @RequestBody Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                // Сохраняем старого тренера для обновления счетчика
                Integer oldTrainerId = members.get(i).getTrainerId();
                Integer newTrainerId = updatedMember.getTrainerId();
                
                // Обновляем счетчики тренеров
                updateTrainerCounters(oldTrainerId, newTrainerId);
                
                updatedMember.setId(id);
                members.set(i, updatedMember);
                return "Информация о члене успешно обновлена!";
            }
        }
        return "Ошибка: Член с ID " + id + " не найден";
    }
    
    // 5. Удалить члена
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable int id) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                // Уменьшаем счетчик у тренера, если член был прикреплен
                Integer trainerId = members.get(i).getTrainerId();
                if (trainerId != null) {
                    trainers.stream()
                            .filter(t -> t.getId() == trainerId)
                            .findFirst()
                            .ifPresent(Trainer::decrementClients);
                }
                
                members.remove(i);
                return "Член успешно удален!";
            }
        }
        return "Ошибка: Член с ID " + id + " не найден";
    }
    
    // 6. Получить количество членов
    @GetMapping("/count")
    public int getMembersCount() {
        return members.size();
    }
    
    // 7. Поиск членов по типу членства
    @GetMapping("/type/{membershipType}")
    public List<Member> getMembersByType(@PathVariable String membershipType) {
        return members.stream()
                .filter(member -> member.getMembershipType().equalsIgnoreCase(membershipType))
                .collect(Collectors.toList());
    }
    
    // 8. Получить членов по тренеру
    @GetMapping("/trainer/{trainerId}")
    public List<Member> getMembersByTrainer(@PathVariable int trainerId) {
        return members.stream()
                .filter(member -> member.getTrainerId() != null && 
                        member.getTrainerId() == trainerId)
                .collect(Collectors.toList());
    }
    
    // 9. Поиск членов по имени
    @GetMapping("/search")
    public List<Member> searchMembersByName(@RequestParam String name) {
        return members.stream()
                .filter(member -> member.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // 10. Очистить список членов
    @DeleteMapping("/clear")
    public String clearAllMembers(@RequestParam(required = false, defaultValue = "NO") String confirmation) {
        if ("YES".equalsIgnoreCase(confirmation)) {
            // Сбрасываем счетчики у всех тренеров
            for (Member member : members) {
                if (member.getTrainerId() != null) {
                    trainers.stream()
                            .filter(t -> t.getId() == member.getTrainerId())
                            .findFirst()
                            .ifPresent(Trainer::decrementClients);
                }
            }
            
            int count = members.size();
            members.clear();
            nextId = 1;
            return "Удалены все члены (" + count + " записей)";
        }
        return "Операция отменена. Для очистки списка используйте параметр confirmation=YES";
    }
    
    // Вспомогательные методы
    
    private boolean isValidMembershipType(String type) {
        String[] validTypes = {"Basic", "Silver", "Gold", "Premium"};
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
    
    private void updateTrainerCounters(Integer oldTrainerId, Integer newTrainerId) {
        // Если тренер изменился
        if (oldTrainerId != null && newTrainerId != null && !oldTrainerId.equals(newTrainerId)) {
            // Уменьшаем у старого тренера
            trainers.stream()
                    .filter(t -> t.getId() == oldTrainerId)
                    .findFirst()
                    .ifPresent(Trainer::decrementClients);
            
            // Увеличиваем у нового тренера
            trainers.stream()
                    .filter(t -> t.getId() == newTrainerId)
                    .findFirst()
                    .ifPresent(t -> {
                        if (t.hasAvailableSlots()) {
                            t.incrementClients();
                        }
                    });
        }
        // Если тренера раньше не было, а теперь есть
        else if (oldTrainerId == null && newTrainerId != null) {
            trainers.stream()
                    .filter(t -> t.getId() == newTrainerId)
                    .findFirst()
                    .ifPresent(t -> {
                        if (t.hasAvailableSlots()) {
                            t.incrementClients();
                        }
                    });
        }
        // Если тренер был, а теперь удален
        else if (oldTrainerId != null && newTrainerId == null) {
            trainers.stream()
                    .filter(t -> t.getId() == oldTrainerId)
                    .findFirst()
                    .ifPresent(Trainer::decrementClients);
        }
    }
}