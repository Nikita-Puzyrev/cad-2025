package com.musicstore.controller;

import com.musicstore.model.Equipment;
import com.musicstore.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web")
public class EquipmentWebController {
    
    private final EquipmentService equipmentService;
    
    @Autowired
    public EquipmentWebController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }
    
    // Главная страница
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("totalEquipment", equipmentService.getTotalEquipmentCount());
        model.addAttribute("totalItems", equipmentService.getTotalItemsCount());
        model.addAttribute("totalValue", equipmentService.getTotalInventoryValue());
        return "index";
    }
    
    // Список всего оборудования
    @GetMapping("/equipment")
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "equipment-list";
    }
    
    // Форма добавления нового оборудования
    @GetMapping("/equipment/add")
    public String showAddForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "add-equipment";
    }
    
    // Добавление нового оборудования
    @PostMapping("/equipment/add")
    public String addEquipment(@ModelAttribute Equipment equipment, Model model) {
        try {
            equipmentService.saveEquipment(equipment);
            return "redirect:/web/equipment";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("equipment", equipment);
            model.addAttribute("categories", equipmentService.getAllCategories());
            return "add-equipment";
        }
    }
    
    // Форма редактирования оборудования
    @GetMapping("/equipment/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.getEquipmentById(id)
            .orElseThrow(() -> new IllegalArgumentException("Оборудование не найдено"));
        model.addAttribute("equipment", equipment);
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "edit-equipment";
    }
    
    // Обновление оборудования
    @PostMapping("/equipment/edit/{id}")
    public String updateEquipment(@PathVariable Long id, 
                                 @ModelAttribute Equipment equipment, 
                                 Model model) {
        try {
            equipment.setId(id);
            equipmentService.saveEquipment(equipment);
            return "redirect:/web/equipment";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("equipment", equipment);
            model.addAttribute("categories", equipmentService.getAllCategories());
            return "edit-equipment";
        }
    }
    
    // Удаление оборудования (с подтверждением)
    @GetMapping("/equipment/delete/{id}")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return "redirect:/web/equipment";
    }
    
    // Поиск оборудования
    @GetMapping("/equipment/search")
    public String searchEquipment(@RequestParam(required = false) String keyword, 
                                 @RequestParam(required = false) String categoryId,
                                 Model model) {
        List<Equipment> results;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = equipmentService.searchEquipment(keyword);
        } else if (categoryId != null && !categoryId.trim().isEmpty()) {
            results = equipmentService.getEquipmentByCategory(Long.parseLong(categoryId));
        } else {
            results = equipmentService.getAllEquipment();
        }
        
        model.addAttribute("equipmentList", results);
        model.addAttribute("categories", equipmentService.getAllCategories());
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        return "equipment-list";
    }
    
    // Статистика
    @GetMapping("/stats")
    public String showStats(Model model) {
        model.addAttribute("totalEquipment", equipmentService.getTotalEquipmentCount());
        model.addAttribute("totalItems", equipmentService.getTotalItemsCount());
        model.addAttribute("totalValue", equipmentService.getTotalInventoryValue());
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        return "stats";
    }
}