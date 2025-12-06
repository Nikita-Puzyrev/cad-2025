package com.musicstore.controller;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    
    // Класс для хранения информации об одном товаре
    public static class Equipment {
        private int id;
        private String name;
        private String type;
        private String brand;
        private double price;
        private int quantity;
        private LocalDateTime addedDate;
        
        public Equipment(int id, String name, String type, 
                        String brand, double price, int quantity) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.brand = brand;
            this.price = price;
            this.quantity = quantity;
            this.addedDate = LocalDateTime.now();
        }
        
        // Геттеры
        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getBrand() { return brand; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public LocalDateTime getAddedDate() { return addedDate; }
        
        // Сеттеры
        public void setName(String name) { this.name = name; }
        public void setType(String type) { this.type = type; }
        public void setBrand(String brand) { this.brand = brand; }
        public void setPrice(double price) { this.price = price; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
    
    private List<Equipment> equipmentList = new ArrayList<>();
    private int nextId = 1;
    
    // Инициализация с начальными данными
    public EquipmentController() {
        equipmentList.add(new Equipment(nextId++, "Fender Stratocaster", 
            "Гитара", "Fender", 1200.00, 5));
        equipmentList.add(new Equipment(nextId++, "Yamaha P-125", 
            "Клавиши", "Yamaha", 800.00, 3));
        equipmentList.add(new Equipment(nextId++, "Pearl Export", 
            "Ударные", "Pearl", 1500.00, 2));
        
        System.out.println("На склад добавлено " + equipmentList.size() + " товаров");
    }
    
    // 1. Получить все оборудование
    @GetMapping("/")
    public List<Equipment> getAllEquipment() {
        return equipmentList;
    }
    
    // 2. Получить оборудование по ID
    @GetMapping("/{id}")
    public Object getEquipmentById(@PathVariable int id) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getId() == id) {
                return equipment;
            }
        }
        return Map.of("error", "Оборудование с ID " + id + " не найдено");
    }
    
    // 3. Добавить новое оборудование
    @PostMapping("/")
    public Map<String, String> addEquipment(@RequestBody Equipment newEquipment) {
        // Проверки
        if (newEquipment.getName() == null || newEquipment.getName().trim().isEmpty()) {
            return Map.of("error", "Название товара обязательно");
        }
        if (newEquipment.getType() == null || newEquipment.getType().trim().isEmpty()) {
            return Map.of("error", "Тип товара обязателен");
        }
        if (newEquipment.getBrand() == null || newEquipment.getBrand().trim().isEmpty()) {
            return Map.of("error", "Бренд товара обязателен");
        }
        if (newEquipment.getPrice() <= 0) {
            return Map.of("error", "Цена должна быть больше 0");
        }
        if (newEquipment.getQuantity() < 0) {
            return Map.of("error", "Количество не может быть отрицательным");
        }
        
        Equipment equipment = new Equipment(
            nextId++,
            newEquipment.getName(),
            newEquipment.getType(),
            newEquipment.getBrand(),
            newEquipment.getPrice(),
            newEquipment.getQuantity()
        );
        
        equipmentList.add(equipment);
        return Map.of(
            "message", "Оборудование успешно добавлено",
            "id", String.valueOf(equipment.getId())
        );
    }
    
    // 4. Обновить оборудование
    @PutMapping("/{id}")
    public Map<String, String> updateEquipment(@PathVariable int id, 
                                              @RequestBody Equipment updatedEquipment) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getId() == id) {
                if (updatedEquipment.getName() != null && 
                    !updatedEquipment.getName().trim().isEmpty()) {
                    equipment.setName(updatedEquipment.getName());
                }
                if (updatedEquipment.getType() != null && 
                    !updatedEquipment.getType().trim().isEmpty()) {
                    equipment.setType(updatedEquipment.getType());
                }
                if (updatedEquipment.getBrand() != null && 
                    !updatedEquipment.getBrand().trim().isEmpty()) {
                    equipment.setBrand(updatedEquipment.getBrand());
                }
                if (updatedEquipment.getPrice() > 0) {
                    equipment.setPrice(updatedEquipment.getPrice());
                }
                if (updatedEquipment.getQuantity() >= 0) {
                    equipment.setQuantity(updatedEquipment.getQuantity());
                }
                return Map.of("message", "Оборудование с ID " + id + " успешно обновлено");
            }
        }
        return Map.of("error", "Оборудование с ID " + id + " не найдено");
    }
    
    // 5. Удалить оборудование
    @DeleteMapping("/{id}")
    public Map<String, String> deleteEquipment(@PathVariable int id) {
        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getId() == id) {
                equipmentList.remove(i);
                return Map.of("message", "Оборудование с ID " + id + " успешно удалено");
            }
        }
        return Map.of("error", "Оборудование с ID " + id + " не найдено");
    }
    
    // 6. Получить статистику
    @GetMapping("/count")
    public Map<String, Integer> getEquipmentCount() {
        int totalItems = 0;
        for (Equipment equipment : equipmentList) {
            totalItems += equipment.getQuantity();
        }
        return Map.of("totalEquipment", equipmentList.size(), 
                     "totalItems", totalItems);
    }
    
    // 7. Очистить весь склад
    @DeleteMapping("/clear")
    public Map<String, String> clearAllEquipment() {
        int count = equipmentList.size();
        equipmentList.clear();
        nextId = 1;
        return Map.of("message", "Весь склад очищен. Удалено " + count + " позиций");
    }
    
    // 8. Поиск по типу
    @GetMapping("/type/{type}")
    public List<Equipment> getEquipmentByType(@PathVariable String type) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment equipment : equipmentList) {
            if (equipment.getType().equalsIgnoreCase(type)) {
                result.add(equipment);
            }
        }
        return result;
    }
    
    // 9. Поиск по бренду
    @GetMapping("/brand/{brand}")
    public List<Equipment> getEquipmentByBrand(@PathVariable String brand) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment equipment : equipmentList) {
            if (equipment.getBrand().equalsIgnoreCase(brand)) {
                result.add(equipment);
            }
        }
        return result;
    }
    
    // 10. Дополнительный метод: получить общую стоимость склада
    @GetMapping("/total-value")
    public Map<String, Double> getTotalValue() {
        double totalValue = 0;
        for (Equipment equipment : equipmentList) {
            totalValue += equipment.getPrice() * equipment.getQuantity();
        }
        return Map.of("totalValue", totalValue);
    }
}