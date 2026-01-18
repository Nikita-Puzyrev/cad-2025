package com.example.university.departments.module.service;

import com.example.university.departments.module.model.Department;
import com.example.university.departments.module.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Получение всех департаментов
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Получение конкретного департамента по Id
    public Department getDepartmentById(long id) {
        Optional<Department> dep = departmentRepository.findById(id);
        return dep.orElse(null);
    }

    // Сохранение департамента (создание или обновление)
    @Transactional
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    // Удаление департамента
    public void deleteDepartment(long id) {
        departmentRepository.deleteById(id);
    }
}