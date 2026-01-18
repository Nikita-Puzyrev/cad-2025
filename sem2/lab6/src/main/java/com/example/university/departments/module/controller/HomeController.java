package com.example.university.departments.module.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        // Здесь можно передавать дополнительные данные в модель
        return "home"; // Имя шаблона Thymeleaf
    }
}
