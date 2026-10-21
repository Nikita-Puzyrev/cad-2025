package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Workout;
import com.fitness.fitness_center.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    
    private final WorkoutService workoutService;
    
    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }
    
    @GetMapping
    public String index(Model model) {
        model.addAttribute("workouts", workoutService.getAllWorkouts());
        model.addAttribute("workout", new Workout());
        model.addAttribute("stats", workoutService.getWorkoutStats());
        return "workouts/index";
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public String addWorkout(@ModelAttribute Workout workout, RedirectAttributes redirectAttributes) {
        if (workout.getTitle() == null || workout.getTitle().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Название не может быть пустым");
            return "redirect:/workouts";
        }
        
        workoutService.addWorkout(workout);
        redirectAttributes.addFlashAttribute("success", "Тренировка добавлена!");
        return "redirect:/workouts";
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String editWorkout(@PathVariable Long id, Model model) {
        workoutService.getWorkoutById(id).ifPresent(workout -> {
            model.addAttribute("workout", workout);
        });
        return "workouts/edit";
    }
    
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String updateWorkout(@PathVariable Long id, @ModelAttribute Workout workout, 
                               RedirectAttributes redirectAttributes) {
        workout.setId(id);
        workoutService.updateWorkout(workout);
        redirectAttributes.addFlashAttribute("success", "Тренировка обновлена!");
        return "redirect:/workouts";
    }
    
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteWorkout(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = workoutService.deleteWorkout(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Тренировка удалена!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Тренировка не найдена!");
        }
        return "redirect:/workouts";
    }
    
    @GetMapping("/complete/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String completeWorkout(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean completed = workoutService.markWorkoutAsCompleted(id);
        if (completed) {
            redirectAttributes.addFlashAttribute("success", "Тренировка отмечена как выполненная!");
        }
        return "redirect:/workouts";
    }
    
    @GetMapping("/pending/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String pendingWorkout(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean pending = workoutService.markWorkoutAsPending(id);
        if (pending) {
            redirectAttributes.addFlashAttribute("success", "Тренировка отмечена как ожидающая!");
        }
        return "redirect:/workouts";
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String getStats(Model model) {
        model.addAttribute("stats", workoutService.getWorkoutStats());
        return "workouts/stats";
    }
}