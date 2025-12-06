package com.musicstore.model;

import java.time.LocalDateTime;

public class Equipment {
    private Long id;
    private String name;
    private String type;
    private String brand;
    private Double price;
    private Integer quantity;
    private Category category;
    private LocalDateTime addedDate;
    private String description;
    
    // Конструкторы
    public Equipment() {
        this.addedDate = LocalDateTime.now();
    }
    
    public Equipment(Long id, String name, String type, String brand, 
                    Double price, Integer quantity, Category category, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.addedDate = LocalDateTime.now();
    }
    
    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    // Метод для расчета общей стоимости
    public Double getTotalValue() {
        return price * quantity;
    }
}