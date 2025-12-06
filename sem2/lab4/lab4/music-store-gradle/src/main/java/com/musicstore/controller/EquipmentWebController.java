package com.musicstore.controller;

import com.musicstore.model.Equipment;
import com.musicstore.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("isAuthenticated", auth.isAuthenticated() && !auth.getName().equals("anonymousUser"));
        
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("totalEquipment", equipmentService.getTotalEquipmentCount());
        model.addAttribute("totalItems", equipmentService.getTotalItemsCount());
        model.addAttribute("totalValue", equipmentService.getTotalInventoryValue());
        return "index";
    }
    
    // Список всего оборудования (доступно всем)
    @GetMapping("/equipment")
    public String listEquipment(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("isAuthenticated", auth.isAuthenticated() && !auth.getName().equals("anonymousUser"));
        
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "equipment-list";
    }
    
    // Форма добавления нового оборудования (только для авторизованных)
    @GetMapping("/equipment/add")
    @PreAuthorize("hasRole('USER')")
    public String showAddForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "add-equipment";
    }
    
    // Добавление нового оборудования (только для авторизованных)
    @PostMapping("/equipment/add")
    @PreAuthorize("hasRole('USER')")
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
    
    // Форма редактирования оборудования (только для MODERATOR и ADMIN)
    @GetMapping("/equipment/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.getEquipmentById(id)
            .orElseThrow(() -> new IllegalArgumentException("Оборудование не найдено"));
        model.addAttribute("equipment", equipment);
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "edit-equipment";
    }
    
    // Обновление оборудования (только для MODERATOR и ADMIN)
    @PostMapping("/equipment/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
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
    
    // Удаление оборудования (только для MODERATOR и ADMIN)
    @GetMapping("/equipment/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return "redirect:/web/equipment";
    }
    
    // Поиск оборудования (доступно всем)
    @GetMapping("/equipment/search")
    public String searchEquipment(@RequestParam(required = false) String keyword, 
                                 @RequestParam(required = false) String categoryId,
                                 Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("isAuthenticated", auth.isAuthenticated() && !auth.getName().equals("anonymousUser"));
        
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
    
    // Статистика (только для авторизованных)
    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER')")
    public String showStats(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("isAuthenticated", true);
        
        model.addAttribute("totalEquipment", equipmentService.getTotalEquipmentCount());
        model.addAttribute("totalItems", equipmentService.getTotalItemsCount());
        model.addAttribute("totalValue", equipmentService.getTotalInventoryValue());
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        return "stats";
    }
}