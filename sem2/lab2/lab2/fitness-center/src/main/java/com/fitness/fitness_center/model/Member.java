package com.fitness.fitness_center.model;

public class Member {
    private int id;
    private String name;
    private String membershipType;
    private int age;
    private String phoneNumber;
    private String email;
    private Integer trainerId; // ID тренера, к которому прикреплен член
    
    public Member() {
    }
    
    public Member(int id, String name, String membershipType, int age, 
                  String phoneNumber, String email, Integer trainerId) {
        this.id = id;
        this.name = name;
        this.membershipType = membershipType;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.trainerId = trainerId;
    }
    
    // Геттеры и сеттеры
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
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
    
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", membershipType='" + membershipType + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", trainerId=" + trainerId +
                '}';
    }
}