package com.musicstore.service;

import com.musicstore.model.Equipment;
import com.musicstore.model.Category;
import com.musicstore.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {
    
    private final EquipmentRepository equipmentRepository;
    
    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }
    
    // Методы для Equipment
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
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
        return equipmentRepository.findAllCategories();
    }
    
    public Optional<Category> getCategoryById(Long id) {
        return equipmentRepository.findCategoryById(id);
    }
    
    // Поисковые методы
    public List<Equipment> getEquipmentByType(String type) {
        return equipmentRepository.findByType(type);
    }
    
    public List<Equipment> getEquipmentByBrand(String brand) {
        return equipmentRepository.findByBrand(brand);
    }
    
    public List<Equipment> getEquipmentByCategory(Long categoryId) {
        return equipmentRepository.findByCategoryId(categoryId);
    }
    
    public List<Equipment> searchEquipment(String keyword) {
        return equipmentRepository.searchByName(keyword);
    }
    
    // Статистика
    public Integer getTotalEquipmentCount() {
        return equipmentRepository.getTotalEquipmentCount();
    }
    
    public Integer getTotalItemsCount() {
        return equipmentRepository.getTotalItemsCount();
    }
    
    public Double getTotalInventoryValue() {
        return equipmentRepository.getTotalValue();
    }
}