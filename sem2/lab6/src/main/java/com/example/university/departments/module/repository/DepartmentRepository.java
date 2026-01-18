package com.example.university.departments.module.repository;

import com.example.university.departments.module.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> { }