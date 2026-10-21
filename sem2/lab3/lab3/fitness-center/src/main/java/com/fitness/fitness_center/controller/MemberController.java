package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Member;
import com.fitness.fitness_center.model.Trainer;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    public MemberController() {
        trainers.add(new Trainer(1, "Александр Иванов", "Силовые тренировки", 
                5, "+79161112233", "alex@fitness.com", 
                "Пн-Пт: 9:00-18:00", 2000.0, 10));
        
        members.add(new Member(nextId++, "Иван Петров", "Gold", 28, 
                "+79161234567", "ivan@email.com", 1));
        members.add(new Member(nextId++, "Анна Сидорова", "Silver", 25, 
                "+79169876543", "anna@email.com", 1));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public String index(Model model) {
        model.addAttribute("members", members);
        model.addAttribute("member", new Member());
        model.addAttribute("trainers", trainers);
        return "members/index";
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String addMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Имя не может быть пустым");
            return "redirect:/members";
        }
        
        if (member.getAge() <= 0 || member.getAge() > 100) {
            redirectAttributes.addFlashAttribute("error", "Возраст должен быть от 1 до 100 лет");
            return "redirect:/members";
        }
        
        member.setId(nextId++);
        members.add(member);
        redirectAttributes.addFlashAttribute("success", "Член добавлен!");
        return "redirect:/members";
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String editMember(@PathVariable int id, Model model) {
        Member member = getMemberById(id);
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("trainers", trainers);
            return "members/edit";
        }
        return "redirect:/members";
    }
    
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String updateMember(@PathVariable int id, @ModelAttribute Member member, 
                              RedirectAttributes redirectAttributes) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                member.setId(id);
                members.set(i, member);
                redirectAttributes.addFlashAttribute("success", "Данные обновлены!");
                break;
            }
        }
        return "redirect:/members";
    }
    
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMember(@PathVariable int id, RedirectAttributes redirectAttributes) {
        boolean removed = members.removeIf(member -> member.getId() == id);
        if (removed) {
            redirectAttributes.addFlashAttribute("success", "Член удален!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Член не найден!");
        }
        return "redirect:/members";
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String getStats(Model model) {
        int totalMembers = members.size();
        int goldMembers = (int) members.stream()
                .filter(m -> "Gold".equalsIgnoreCase(m.getMembershipType()))
                .count();
        int silverMembers = (int) members.stream()
                .filter(m -> "Silver".equalsIgnoreCase(m.getMembershipType()))
                .count();
        
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("goldMembers", goldMembers);
        model.addAttribute("silverMembers", silverMembers);
        
        return "members/stats";
    }
    
    private Member getMemberById(int id) {
        return members.stream()
                .filter(member -> member.getId() == id)
                .findFirst()
                .orElse(null);
    }
}