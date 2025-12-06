package com.musicstore.service;

import com.musicstore.model.Equipment;
import com.musicstore.model.Category;
import com.musicstore.repository.EquipmentRepository;
import com.musicstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipmentService {
    
    private final EquipmentRepository equipmentRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository, 
                           CategoryRepository categoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.categoryRepository = categoryRepository;
        initializeData();
    }
    
    // Инициализация начальных данных
    private void initializeData() {
        // Проверяем, есть ли уже данные в БД
        if (categoryRepository.count() == 0) {
            // Создаем категории
            Category guitars = new Category("Гитары", "Акустические и электрические гитары");
            Category keyboards = new Category("Клавишные", "Синтезаторы, цифровые пианино");
            Category drums = new Category("Ударные", "Барабанные установки и перкуссия");
            Category amps = new Category("Усилители", "Гитарные и басовые усилители");
            
            categoryRepository.save(guitars);
            categoryRepository.save(keyboards);
            categoryRepository.save(drums);
            categoryRepository.save(amps);
            
            // Создаем оборудование
            Equipment equipment1 = new Equipment("Fender Stratocaster", 
                "Электрогитара", "Fender", 1200.00, 5, guitars, "Классическая электрогитара");
            Equipment equipment2 = new Equipment("Yamaha P-125", 
                "Цифровое пианино", "Yamaha", 800.00, 3, keyboards, "Портативное цифровое пианино");
            Equipment equipment3 = new Equipment("Pearl Export", 
                "Барабанная установка", "Pearl", 1500.00, 2, drums, "Комплект для начинающих");
            Equipment equipment4 = new Equipment("Marshall DSL40CR", 
                "Гитарный комбоусилитель", "Marshall", 900.00, 4, amps, "Трубный усилитель 40Вт");
            
            equipmentRepository.save(equipment1);
            equipmentRepository.save(equipment2);
            equipmentRepository.save(equipment3);
            equipmentRepository.save(equipment4);
            
            System.out.println("Начальные данные загружены в базу данных");
        }
    }
    
    // Методы для Equipment
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAllOrderByDateDesc();
    }
    
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }
    
    public Equipment saveEquipment(Equipment equipment) {
        // Валидация
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название оборудования обязательно");
        }
        if (equipment.getPrice() == null || equipment.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной");
        }
        if (equipment.getQuantity() == null || equipment.getQuantity() < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        }
        
        return equipmentRepository.save(equipment);
    }
    
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }
    
    // Методы для Category
    public List<Category> getAllCategories() {
        return categoryRepository.findAllOrderByName();
    }
    
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    
    // Поисковые методы
    public List<Equipment> getEquipmentByType(String type) {
        return equipmentRepository.findByTypeContainingIgnoreCase(type);
    }
    
    public List<Equipment> getEquipmentByBrand(String brand) {
        return equipmentRepository.findByBrandContainingIgnoreCase(brand);
    }
    
    public List<Equipment> getEquipmentByCategory(Long categoryId) {
        return equipmentRepository.findByCategoryId(categoryId);
    }
    
    public List<Equipment> searchEquipment(String keyword) {
        return equipmentRepository.searchByKeyword(keyword);
    }
    
    // Статистика
    public Integer getTotalEquipmentCount() {
        Long count = equipmentRepository.countAllEquipment();
        return count != null ? count.intValue() : 0;
    }
    
    public Integer getTotalItemsCount() {
        Long sum = equipmentRepository.sumAllQuantities();
        return sum != null ? sum.intValue() : 0;
    }
    
    public Double getTotalInventoryValue() {
        Double total = equipmentRepository.calculateTotalValue();
        return total != null ? total : 0.0;
    }
    
    // Дополнительные методы
    public Long getEquipmentCountByCategory(Long categoryId) {
        return categoryRepository.countEquipmentByCategoryId(categoryId);
    }
    
    public void deleteCategory(Long id) {
        // Проверяем, есть ли оборудование в этой категории
        Long equipmentCount = getEquipmentCountByCategory(id);
        if (equipmentCount > 0) {
            throw new IllegalStateException("Невозможно удалить категорию, в которой есть оборудование. " +
                                           "Сначала удалите или переместите оборудование.");
        }
        categoryRepository.deleteById(id);
    }
}