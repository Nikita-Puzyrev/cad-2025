package com.fitness.fitness_center.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trainers")
public class Trainer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    private String specialization;
    
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    private String email;
    
    private String schedule;
    
    @Column(name = "hourly_rate")
    private Double hourlyRate;
    
    @Column(name = "max_clients")
    private Integer maxClients;
    
    @Column(name = "current_clients")
    private Integer currentClients = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Конструкторы
    public Trainer() {}
    
    public Trainer(String name, String specialization, Integer experienceYears, 
                   String phoneNumber, String email, String schedule, 
                   Double hourlyRate, Integer maxClients) {
        this.name = name;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.schedule = schedule;
        this.hourlyRate = hourlyRate;
        this.maxClients = maxClients;
        this.currentClients = 0;
    }
    
    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public Integer getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public Double getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public Integer getMaxClients() {
        return maxClients;
    }
    
    public void setMaxClients(Integer maxClients) {
        this.maxClients = maxClients;
    }
    
    public Integer getCurrentClients() {
        return currentClients;
    }
    
    public void setCurrentClients(Integer currentClients) {
        this.currentClients = currentClients;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Вспомогательные методы
    public boolean hasAvailableSlots() {
        return currentClients < maxClients;
    }
    
    public void incrementClients() {
        if (hasAvailableSlots()) {
            currentClients++;
        }
    }
    
    public void decrementClients() {
        if (currentClients > 0) {
            currentClients--;
        }
    }
}