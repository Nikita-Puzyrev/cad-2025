package com.musicstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "У вас нет прав для доступа к этой странице!");
        return "access-denied";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        // информация о пользователе
        return "profile";
    }
}