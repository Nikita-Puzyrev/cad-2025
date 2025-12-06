package com.musicstore.controller;

import com.musicstore.model.Category;
import com.musicstore.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/categories")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class CategoryController {
    
    private final EquipmentService equipmentService;
    
    @Autowired
    public CategoryController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }
    
    // Список категорий
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", equipmentService.getAllCategories());
        return "category-list";
    }
    
    // Форма добавления категории
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }
    
    // Добавление категории
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, 
                             RedirectAttributes redirectAttributes) {
        try {
            equipmentService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Категория успешно добавлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении категории: " + e.getMessage());
        }
        return "redirect:/web/categories";
    }
    
    // Форма редактирования категории
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = equipmentService.getCategoryById(id)
            .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        model.addAttribute("category", category);
        return "edit-category";
    }
    
    // Обновление категории
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, 
                                @ModelAttribute Category category,
                                RedirectAttributes redirectAttributes) {
        try {
            category.setId(id);
            equipmentService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Категория успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении категории: " + e.getMessage());
        }
        return "redirect:/web/categories";
    }
    
    // Удаление категории
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, 
                                RedirectAttributes redirectAttributes) {
        try {
            equipmentService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Категория успешно удалена");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении категории");
        }
        return "redirect:/web/categories";
    }
}