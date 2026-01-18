/*
package com.example.university.departments.module.service.impl;

import com.example.university.departments.module.dto.DepartmentCreateDto;
import com.example.university.departments.module.dto.DepartmentResponseDto;
import com.example.university.departments.module.exception.ResourceNotFoundException;
import com.example.university.departments.module.model.Department;
import com.example.university.departments.module.repository.DepartmentRepository;
import com.example.university.departments.module.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponseDto addDepartment(DepartmentCreateDto request) {
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Department savedDepartment = departmentRepository.save(department);
        return toResponseDto(savedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) throws ResourceNotFoundException {
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Департамент не найден");
        }
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public DepartmentResponseDto updateDepartment(Long departmentId, DepartmentCreateDto request) throws ResourceNotFoundException {
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Департамент не найден");
        }
        Department department = departmentOpt.get();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department updatedDepartment = departmentRepository.save(department);
        return toResponseDto(updatedDepartment);
    }

    @Override
    public List<DepartmentResponseDto> listAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private DepartmentResponseDto toResponseDto(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}

 */