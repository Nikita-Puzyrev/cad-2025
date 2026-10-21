package com.fitness.fitness_center.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Value("${app.welcome.message}")
    private String welcomeMessage;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("welcomeMessage", welcomeMessage);
        return "home";
    }
}