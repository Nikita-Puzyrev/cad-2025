package com.musicstore.controller;

import com.musicstore.model.Equipment;
import com.musicstore.model.Category;
import com.musicstore.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EquipmentController {
    
    private final EquipmentService equipmentService;
    
    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }
    
    // 1. Получить все оборудование (доступно всем)
    @GetMapping("/equipment")
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }
    
    // 2. Получить оборудование по ID (доступно всем)
    @GetMapping("/equipment/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.getEquipmentById(id);
        if (equipment.isPresent()) {
            return ResponseEntity.ok(equipment.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Оборудование с ID " + id + " не найдено");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // 3. Добавить новое оборудование (только для USER, MODERATOR, ADMIN)
    @PostMapping("/equipment")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addEquipment(@RequestBody Equipment equipment) {
        try {
            Equipment savedEquipment = equipmentService.saveEquipment(equipment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // 4. Обновить оборудование (только для MODERATOR и ADMIN)
    @PutMapping("/equipment/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEquipment(@PathVariable Long id, 
                                            @RequestBody Equipment equipment) {
        equipment.setId(id);
        try {
            Equipment updatedEquipment = equipmentService.saveEquipment(equipment);
            return ResponseEntity.ok(updatedEquipment);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // 5. Удалить оборудование (только для MODERATOR и ADMIN)
    @DeleteMapping("/equipment/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        if (!equipmentService.getEquipmentById(id).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Оборудование с ID " + id + " не найдено");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        equipmentService.deleteEquipment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Оборудование с ID " + id + " удалено");
        return ResponseEntity.ok(response);
    }
    
    // 6. Поиск по названию (доступно всем)
    @GetMapping("/equipment/search")
    public List<Equipment> searchEquipment(@RequestParam String keyword) {
        return equipmentService.searchEquipment(keyword);
    }
    
    // 7. Получить оборудование по категории (доступно всем)
    @GetMapping("/equipment/category/{categoryId}")
    public List<Equipment> getEquipmentByCategory(@PathVariable Long categoryId) {
        return equipmentService.getEquipmentByCategory(categoryId);
    }
    
    // 8. Получить все категории (доступно всем)
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return equipmentService.getAllCategories();
    }
    
    // 9. Получить категорию по ID (доступно всем)
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = equipmentService.getCategoryById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Категория с ID " + id + " не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // 10. Статистика (доступно всем)
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEquipment", equipmentService.getTotalEquipmentCount());
        stats.put("totalItems", equipmentService.getTotalItemsCount());
        stats.put("totalValue", equipmentService.getTotalInventoryValue());
        return stats;
    }
    
    // 11. Поиск по типу (доступно всем)
    @GetMapping("/equipment/type/{type}")
    public List<Equipment> getEquipmentByType(@PathVariable String type) {
        return equipmentService.getEquipmentByType(type);
    }
    
    // 12. Поиск по бренду (доступно всем)
    @GetMapping("/equipment/brand/{brand}")
    public List<Equipment> getEquipmentByBrand(@PathVariable String brand) {
        return equipmentService.getEquipmentByBrand(brand);
    }
}