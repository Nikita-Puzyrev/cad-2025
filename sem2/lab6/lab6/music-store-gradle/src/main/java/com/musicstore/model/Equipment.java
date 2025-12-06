package com.musicstore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
public class Equipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate;
    
    // Конструкторы
    public Equipment() {
        this.addedDate = LocalDateTime.now();
    }
    
    public Equipment(String name, String type, String brand, 
                    Double price, Integer quantity, Category category, String description) {
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
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
    
    // Метод для расчета общей стоимости (не сохраняется в БД)
    @Transient
    public Double getTotalValue() {
        return price * quantity;
    }
    
    @Override
    public String toString() {
        return "Equipment{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", brand='" + brand + '\'' +
               ", price=" + price +
               ", quantity=" + quantity +
               ", category=" + (category != null ? category.getName() : "null") +
               ", addedDate=" + addedDate +
               '}';
    }
}