package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Member;
import com.fitness.fitness_center.model.Trainer;
import com.fitness.fitness_center.service.MemberService;
import com.fitness.fitness_center.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private TrainerService trainerService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public String index(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("member", new Member());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        model.addAttribute("totalMembers", memberService.getTotalMembers());
        return "members/index";
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String addMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Имя не может быть пустым");
            return "redirect:/members";
        }
        
        if (member.getAge() == null || member.getAge() <= 0 || member.getAge() > 100) {
            redirectAttributes.addFlashAttribute("error", "Возраст должен быть от 1 до 100 лет");
            return "redirect:/members";
        }
        
        memberService.saveMember(member);
        redirectAttributes.addFlashAttribute("success", "Член добавлен!");
        return "redirect:/members";
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String editMember(@PathVariable Integer id, Model model) {
        memberService.getMemberById(id).ifPresent(member -> {
            model.addAttribute("member", member);
            model.addAttribute("trainers", trainerService.getAllTrainers());
        });
        return "members/edit";
    }
    
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String updateMember(@PathVariable Integer id, @ModelAttribute Member member, 
                              RedirectAttributes redirectAttributes) {
        member.setId(id);
        memberService.saveMember(member);
        redirectAttributes.addFlashAttribute("success", "Данные члена обновлены!");
        return "redirect:/members";
    }
    
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMember(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        memberService.deleteMember(id);
        redirectAttributes.addFlashAttribute("success", "Член удален!");
        return "redirect:/members";
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public String searchMembers(@RequestParam String name, Model model) {
        List<com.fitness.fitness_center.model.Member> searchResults = memberService.searchMembersByName(name);
        List<com.fitness.fitness_center.model.Trainer> trainers = trainerService.getAllTrainers();
        
        model.addAttribute("members", searchResults);
        model.addAttribute("searchQuery", name);
        model.addAttribute("member", new Member());
        model.addAttribute("trainers", trainers);
        return "members/index";
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public String getMembersByType(@PathVariable String type, Model model) {
        List<com.fitness.fitness_center.model.Member> filteredMembers = memberService.getMembersByMembershipType(type);
        List<com.fitness.fitness_center.model.Trainer> trainers = trainerService.getAllTrainers();
        
        model.addAttribute("members", filteredMembers);
        model.addAttribute("filterType", type);
        model.addAttribute("member", new Member());
        model.addAttribute("trainers", trainers);
        return "members/index";
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String getStats(Model model) {
        long totalMembers = memberService.getTotalMembers();
        long goldMembers = memberService.getMembersByTypeCount("Gold");
        long silverMembers = memberService.getMembersByTypeCount("Silver");
        long premiumMembers = memberService.getMembersByTypeCount("Premium");
        
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("goldMembers", goldMembers);
        model.addAttribute("silverMembers", silverMembers);
        model.addAttribute("premiumMembers", premiumMembers);
        
        return "members/stats";
    }
}