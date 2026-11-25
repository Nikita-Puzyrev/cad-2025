package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    // Пустой конструктор обязателен для Hibernate
    public Category() {}

    public Category(int categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    // Геттеры и сеттеры
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}