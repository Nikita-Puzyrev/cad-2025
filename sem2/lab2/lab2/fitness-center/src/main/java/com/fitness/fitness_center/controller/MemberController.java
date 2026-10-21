package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Member;
import com.fitness.fitness_center.model.Trainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/members")
public class MemberController {
    
    private List<Member> members = new ArrayList<>();
    private List<Trainer> trainers = new ArrayList<>();
    private int nextId = 1;
    
    // Инициализация тестовыми данными
    public MemberController() {
        // Инициализируем тренеров
        trainers.add(new Trainer(1, "Александр Иванов", "Силовые тренировки", 
                5, "+79161112233", "alex@fitness.com", 
                "Пн-Пт: 9:00-18:00", 2000.0, 10));
        trainers.add(new Trainer(2, "Екатерина Петрова", "Йога и растяжка", 
                3, "+79162223344", "ekaterina@fitness.com", 
                "Вт-Сб: 10:00-19:00", 1500.0, 8));
        
        // Инициализируем членов
        members.add(new Member(nextId++, "Иван Петров", "Gold", 28, 
                "+79161234567", "ivan@email.com", 1));
        members.add(new Member(nextId++, "Анна Сидорова", "Silver", 25, 
                "+79169876543", "anna@email.com", 1));
        members.add(new Member(nextId++, "Алексей Смирнов", "Premium", 35, 
                "+79165554433", "alex@email.com", 2));
    }
    
    // Главная страница членов
    @GetMapping
    public String index(Model model) {
        model.addAttribute("members", members);
        model.addAttribute("member", new Member()); // Пустой объект для формы
        model.addAttribute("trainers", trainers);
        return "members/index";
    }
    
    // Добавление нового члена
    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        // Проверка на пустые поля
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Имя не может быть пустым");
            return "redirect:/fitness/members";
        }
        
        if (member.getAge() <= 0 || member.getAge() > 100) {
            redirectAttributes.addFlashAttribute("error", "Возраст должен быть от 1 до 100 лет");
            return "redirect:/fitness/members";
        }
        
        // Проверка типа членства
        if (!isValidMembershipType(member.getMembershipType())) {
            redirectAttributes.addFlashAttribute("error", 
                    "Неверный тип членства. Доступные типы: Basic, Silver, Gold, Premium");
            return "redirect:/fitness/members";
        }
        
        member.setId(nextId++);
        members.add(member);
        redirectAttributes.addFlashAttribute("success", "Член успешно добавлен!");
        return "redirect:/fitness/members";
    }
    
    // Страница редактирования члена
    @GetMapping("/edit/{id}")
    public String editMember(@PathVariable int id, Model model) {
        Member member = getMemberById(id);
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("trainers", trainers);
            return "members/edit";
        }
        return "redirect:/fitness/members";
    }
    
    // Обновление члена
    @PostMapping("/update/{id}")
    public String updateMember(@PathVariable int id, @ModelAttribute Member member, 
                              RedirectAttributes redirectAttributes) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                member.setId(id);
                members.set(i, member);
                redirectAttributes.addFlashAttribute("success", "Данные члена обновлены!");
                break;
            }
        }
        return "redirect:/fitness/members";
    }
    
    // Страница подтверждения удаления
    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable int id, Model model) {
        Member member = getMemberById(id);
        if (member != null) {
            model.addAttribute("member", member);
            return "members/confirm-delete";
        }
        return "redirect:/fitness/members";
    }
    
    // Удаление члена
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable int id, RedirectAttributes redirectAttributes) {
        boolean removed = members.removeIf(member -> member.getId() == id);
        if (removed) {
            redirectAttributes.addFlashAttribute("success", "Член успешно удален!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Член не найден!");
        }
        return "redirect:/fitness/members";
    }
    
    // Поиск членов по имени
    @GetMapping("/search")
    public String searchMembers(@RequestParam String name, Model model) {
        List<Member> searchResults = members.stream()
                .filter(member -> member.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        
        model.addAttribute("members", searchResults);
        model.addAttribute("searchQuery", name);
        model.addAttribute("member", new Member());
        model.addAttribute("trainers", trainers);
        return "members/index";
    }
    
    // Фильтр по типу членства
    @GetMapping("/type/{type}")
    public String getMembersByType(@PathVariable String type, Model model) {
        List<Member> filteredMembers = members.stream()
                .filter(member -> member.getMembershipType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        
        model.addAttribute("members", filteredMembers);
        model.addAttribute("filterType", type);
        model.addAttribute("member", new Member());
        model.addAttribute("trainers", trainers);
        return "members/index";
    }
    
    // Статистика
    @GetMapping("/stats")
    public String getStats(Model model) {
        int totalMembers = members.size();
        int goldMembers = (int) members.stream()
                .filter(m -> "Gold".equalsIgnoreCase(m.getMembershipType()))
                .count();
        int silverMembers = (int) members.stream()
                .filter(m -> "Silver".equalsIgnoreCase(m.getMembershipType()))
                .count();
        int premiumMembers = (int) members.stream()
                .filter(m -> "Premium".equalsIgnoreCase(m.getMembershipType()))
                .count();
        
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("goldMembers", goldMembers);
        model.addAttribute("silverMembers", silverMembers);
        model.addAttribute("premiumMembers", premiumMembers);
        
        return "members/stats";
    }
    
    // Вспомогательные методы
    private Member getMemberById(int id) {
        return members.stream()
                .filter(member -> member.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    private boolean isValidMembershipType(String type) {
        String[] validTypes = {"Basic", "Silver", "Gold", "Premium"};
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}