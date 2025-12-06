package com.musicstore.android.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    @SerializedName("id")
    private Long id;
    
    @SerializedName("customerName")
    private String customerName;
    
    @SerializedName("customerEmail")
    private String customerEmail;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("totalAmount")
    private Double totalAmount;
    
    @SerializedName("items")
    private List<OrderItem> items;
    
    public static class OrderItem {
        @SerializedName("productId")
        private Long productId;
        
        @SerializedName("productName")
        private String productName;
        
        @SerializedName("quantity")
        private Integer quantity;
        
        @SerializedName("price")
        private Double price;
        
        // Геттеры и сеттеры
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }
    
    // Геттеры и сеттеры для Order
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    public String getFormattedTotal() {
        return String.format("$%.2f", totalAmount != null ? totalAmount : 0.0);
    }
}