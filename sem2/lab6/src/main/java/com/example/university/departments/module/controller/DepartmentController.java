package com.example.university.departments.module.controller;

import com.example.university.departments.module.model.Department;
import com.example.university.departments.module.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @GetMapping("/form")
    public String showForm() {
        return "departments-form"; // Имя шаблона Thymeleaf
    }

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Показать список департаментов
    @GetMapping("/list")
    public String showDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departments-list"; // шаблон Thymeleaf
    }

    // Показать форму добавления нового департамента
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "department-form";
    }

    // Сохранение нового или редактированного департамента
    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department, BindingResult result) {
        if (result.hasErrors()) {
            return "department-form"; // вернуть обратно на форму при ошибках
        }
        departmentService.saveDepartment(department);
        return "redirect:/departments/list";
    }

    // Показать форму редактирования департамента
    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable("id") long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        return "department-form";
    }

    // Удаление департамента
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable("id") long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments/list";
    }
}

/*
package com.example.university.departments.module.controller;

import com.example.university.departments.module.dto.DepartmentCreateDto;
import com.example.university.departments.module.dto.DepartmentResponseDto;
import com.example.university.departments.module.exception.ResourceNotFoundException;
import com.example.university.departments.module.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Создание нового департамента")
    @PostMapping
    public DepartmentResponseDto createDepartment(@Valid @RequestBody DepartmentCreateDto request) {
        return departmentService.addDepartment(request);
    }

    @Operation(summary = "Получение списка всех департаментов")
    @GetMapping
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentService.listAllDepartments();
    }

    @Operation(summary = "Обновление информации о департаменте")
    @PutMapping("/{departmentId}")
    public DepartmentResponseDto updateDepartment(@PathVariable Long departmentId, @Valid @RequestBody DepartmentCreateDto request) throws ResourceNotFoundException {
        return departmentService.updateDepartment(departmentId, request);
    }

    @Operation(summary = "Удаление департамента")
    @DeleteMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable Long departmentId) throws ResourceNotFoundException {
        departmentService.deleteDepartment(departmentId);
    }
}

 */