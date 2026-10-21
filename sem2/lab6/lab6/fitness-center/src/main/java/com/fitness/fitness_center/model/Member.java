package com.fitness.fitness_center.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "membership_type", nullable = false)
    private String membershipType;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    private String email;
    
    @Column(name = "trainer_id")
    private Integer trainerId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Конструкторы
    public Member() {}
    
    public Member(String name, String membershipType, Integer age, 
                  String phoneNumber, String email, Integer trainerId) {
        this.name = name;
        this.membershipType = membershipType;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.trainerId = trainerId;
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
    
    public String getMembershipType() {
        return membershipType;
    }
    
    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
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
    
    public Integer getTrainerId() {
        return trainerId;
    }
    
    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}