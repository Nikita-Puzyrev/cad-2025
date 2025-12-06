package com.musicstore.repository;

import com.musicstore.model.Equipment;
import com.musicstore.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EquipmentRepository {
    
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private Long nextEquipmentId = 1L;
    private Long nextCategoryId = 1L;
    
    // Инициализация данных
    public EquipmentRepository() {
        // Создаем категории
        Category guitars = new Category(nextCategoryId++, "Гитары", "Акустические и электрические гитары");
        Category keyboards = new Category(nextCategoryId++, "Клавишные", "Синтезаторы, цифровые пианино");
        Category drums = new Category(nextCategoryId++, "Ударные", "Барабанные установки и перкуссия");
        Category amps = new Category(nextCategoryId++, "Усилители", "Гитарные и басовые усилители");
        
        categories.add(guitars);
        categories.add(keyboards);
        categories.add(drums);
        categories.add(amps);
        
        // Добавляем начальное оборудование
        equipmentList.add(new Equipment(nextEquipmentId++, "Fender Stratocaster", 
            "Электрогитара", "Fender", 1200.00, 5, guitars, "Классическая электрогитара"));
        equipmentList.add(new Equipment(nextEquipmentId++, "Yamaha P-125", 
            "Цифровое пианино", "Yamaha", 800.00, 3, keyboards, "Портативное цифровое пианино"));
        equipmentList.add(new Equipment(nextEquipmentId++, "Pearl Export", 
            "Барабанная установка", "Pearl", 1500.00, 2, drums, "Комплект для начинающих"));
        equipmentList.add(new Equipment(nextEquipmentId++, "Marshall DSL40CR", 
            "Гитарный комбоусилитель", "Marshall", 900.00, 4, amps, "Трубный усилитель 40Вт"));
    }
    
    // Методы для Equipment
    public List<Equipment> findAll() {
        return new ArrayList<>(equipmentList);
    }
    
    public Optional<Equipment> findById(Long id) {
        return equipmentList.stream()
            .filter(equipment -> equipment.getId().equals(id))
            .findFirst();
    }
    
    public Equipment save(Equipment equipment) {
        if (equipment.getId() == null) {
            equipment.setId(nextEquipmentId++);
            equipmentList.add(equipment);
        } else {
            // Обновление существующего
            for (int i = 0; i < equipmentList.size(); i++) {
                if (equipmentList.get(i).getId().equals(equipment.getId())) {
                    equipmentList.set(i, equipment);
                    break;
                }
            }
        }
        return equipment;
    }
    
    public void deleteById(Long id) {
        equipmentList.removeIf(equipment -> equipment.getId().equals(id));
    }
    
    // Методы для Category
    public List<Category> findAllCategories() {
        return new ArrayList<>(categories);
    }
    
    public Optional<Category> findCategoryById(Long id) {
        return categories.stream()
            .filter(category -> category.getId().equals(id))
            .findFirst();
    }
    
    // Поисковые методы
    public List<Equipment> findByType(String type) {
        return equipmentList.stream()
            .filter(equipment -> equipment.getType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }
    
    public List<Equipment> findByBrand(String brand) {
        return equipmentList.stream()
            .filter(equipment -> equipment.getBrand().equalsIgnoreCase(brand))
            .collect(Collectors.toList());
    }
    
    public List<Equipment> findByCategoryId(Long categoryId) {
        return equipmentList.stream()
            .filter(equipment -> equipment.getCategory() != null && 
                                equipment.getCategory().getId().equals(categoryId))
            .collect(Collectors.toList());
    }
    
    public List<Equipment> searchByName(String keyword) {
        return equipmentList.stream()
            .filter(equipment -> equipment.getName().toLowerCase()
                .contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    // Статистика
    public Integer getTotalEquipmentCount() {
        return equipmentList.size();
    }
    
    public Integer getTotalItemsCount() {
        return equipmentList.stream()
            .mapToInt(Equipment::getQuantity)
            .sum();
    }
    
    public Double getTotalValue() {
        return equipmentList.stream()
            .mapToDouble(e -> e.getPrice() * e.getQuantity())
            .sum();
    }
}