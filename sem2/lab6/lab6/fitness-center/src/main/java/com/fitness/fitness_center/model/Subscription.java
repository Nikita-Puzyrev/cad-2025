package com.fitness.fitness_center.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "member_id", nullable = false)
    private Integer memberId;
    
    @Column(name = "subscription_type", nullable = false)
    private String subscriptionType;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private String status = "ACTIVE";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Конструкторы
    public Subscription() {}
    
    public Subscription(Integer memberId, String subscriptionType, 
                       LocalDate startDate, LocalDate endDate, Double price) {
        this.memberId = memberId;
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = "ACTIVE";
    }
    
    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
    
    public String getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Вспомогательные методы
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return "ACTIVE".equals(status) && 
               !today.isBefore(startDate) && 
               !today.isAfter(endDate);
    }
}