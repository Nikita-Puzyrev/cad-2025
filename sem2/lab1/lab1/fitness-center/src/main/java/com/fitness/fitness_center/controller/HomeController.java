package com.fitness.fitness_center.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @Value("${app.welcome.message}")
    private String welcomeMessage;
    
    @GetMapping("/")
    public String home() {
        return welcomeMessage;
    }
    
    @GetMapping("/health")
    public String healthCheck() {
        return "Фитнес-центр API работает нормально!";
    }
    
    @GetMapping("/info")
    public String getInfo() {
        return """
               Фитнес-центр Management System
               Версия: 1.0.0
               Автор: Команда разработки
               
               Основные API Endpoints:
               
               Члены фитнес-центра:
               - GET /api/members - получить всех членов
               - GET /api/members/{id} - получить члена по ID
               - POST /api/members - добавить нового члена
               - PUT /api/members/{id} - обновить члена
               - DELETE /api/members/{id} - удалить члена
               
               Тренеры:
               - GET /api/trainers - получить всех тренеров
               - GET /api/trainers/{id} - получить тренера по ID
               - POST /api/trainers - добавить нового тренера
               - PUT /api/trainers/{id} - обновить тренера
               - DELETE /api/trainers/{id} - удалить тренера
               """;
    }
}