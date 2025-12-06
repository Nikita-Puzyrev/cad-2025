package com.musicstore.android.models;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private Long id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("price")
    private Double price;
    
    @SerializedName("stockQuantity")
    private Integer stockQuantity;
    
    @SerializedName("category")
    private String category;
    
    // Конструкторы
    public Product() {}
    
    public Product(String name, String description, Double price, 
                   Integer stockQuantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }
    
    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getFormattedPrice() {
        return String.format("$%.2f", price != null ? price : 0.0);
    }
    
    public String getFormattedStock() {
        return stockQuantity != null ? stockQuantity + " шт." : "0 шт.";
    }
    
    public int getCategoryColor() {
        switch (category != null ? category : "") {
            case "GUITAR":
                return R.color.category_guitar;
            case "AMPLIFIER":
                return R.color.category_amp;
            case "EFFECT":
                return R.color.category_effect;
            case "ACCESSORY":
                return R.color.category_accessory;
            default:
                return R.color.text_secondary;
        }
    }
    
    public String getCategoryName() {
        switch (category != null ? category : "") {
            case "GUITAR":
                return "Гитара";
            case "AMPLIFIER":
                return "Усилитель";
            case "EFFECT":
                return "Эффект";
            case "ACCESSORY":
                return "Аксессуар";
            default:
                return "Не указана";
        }
    }
}