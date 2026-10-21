package com.fitness.fitness_center.model;

import java.util.List;

public class Trainer {
    private int id;
    private String name;
    private String specialization;
    private int experienceYears;
    private String phoneNumber;
    private String email;
    private String schedule;
    private double hourlyRate;
    private int maxClients;
    private int currentClients;
    
    public Trainer() {
    }
    
    public Trainer(int id, String name, String specialization, int experienceYears, 
                   String phoneNumber, String email, String schedule, 
                   double hourlyRate, int maxClients) {
        this.id = id;
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
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public int getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(int experienceYears) {
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
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public int getMaxClients() {
        return maxClients;
    }
    
    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }
    
    public int getCurrentClients() {
        return currentClients;
    }
    
    public void setCurrentClients(int currentClients) {
        this.currentClients = currentClients;
    }
    
    public void incrementClients() {
        if (currentClients < maxClients) {
            currentClients++;
        }
    }
    
    public void decrementClients() {
        if (currentClients > 0) {
            currentClients--;
        }
    }
    
    public boolean hasAvailableSlots() {
        return currentClients < maxClients;
    }
    
    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", experienceYears=" + experienceYears +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", schedule='" + schedule + '\'' +
                ", hourlyRate=" + hourlyRate +
                ", maxClients=" + maxClients +
                ", currentClients=" + currentClients +
                '}';
    }
}