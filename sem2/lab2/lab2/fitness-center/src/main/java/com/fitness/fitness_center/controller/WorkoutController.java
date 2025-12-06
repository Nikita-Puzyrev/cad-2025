package com.fitness.fitness_center.controller;

import com.fitness.fitness_center.model.Workout;
import com.fitness.fitness_center.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    
    private final WorkoutService workoutService;
    
    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }
    
    // Главная страница тренировок
    @GetMapping
    public String index(Model model) {
        model.addAttribute("workouts", workoutService.getAllWorkouts());
        model.addAttribute("workout", new Workout()); // Пустой объект для формы
        model.addAttribute("stats", workoutService.getWorkoutStats());
        return "workouts/index";
    }
    
    // Добавление новой тренировки
    @PostMapping("/add")
    public String addWorkout(@ModelAttribute Workout workout) {
        workoutService.addWorkout(workout);
        return "redirect:/fitness/workouts";
    }
    
    // Страница редактирования тренировки
    @GetMapping("/edit/{id}")
    public String editWorkout(@PathVariable Long id, Model model) {
        workoutService.getWorkoutById(id).ifPresent(workout -> {
            model.addAttribute("workout", workout);
        });
        return "workouts/edit";
    }
    
    // Обновление тренировки
    @PostMapping("/update/{id}")
    public String updateWorkout(@PathVariable Long id, @ModelAttribute Workout workout) {
        workout.setId(id);
        workoutService.updateWorkout(workout);
        return "redirect:/fitness/workouts";
    }
    
    // Удаление тренировки
    @GetMapping("/delete/{id}")
    public String deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return "redirect:/fitness/workouts";
    }
    
    // Страница подтверждения удаления
    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        workoutService.getWorkoutById(id).ifPresent(workout -> {
            model.addAttribute("workout", workout);
        });
        return "workouts/confirm-delete";
    }
    
    // Пометка тренировки как выполненной
    @GetMapping("/complete/{id}")
    public String completeWorkout(@PathVariable Long id) {
        workoutService.markWorkoutAsCompleted(id);
        return "redirect:/fitness/workouts";
    }
    
    // Пометка тренировки как незавершенной
    @GetMapping("/pending/{id}")
    public String pendingWorkout(@PathVariable Long id) {
        workoutService.markWorkoutAsPending(id);
        return "redirect:/fitness/workouts";
    }
    
    // Поиск тренировок по типу
    @GetMapping("/type/{type}")
    public String getWorkoutsByType(@PathVariable String type, Model model) {
        model.addAttribute("workouts", workoutService.findWorkoutsByType(type));
        model.addAttribute("filterType", type);
        model.addAttribute("workout", new Workout());
        return "workouts/index";
    }
    
    // Поиск тренировок по сложности
    @GetMapping("/difficulty/{difficulty}")
    public String getWorkoutsByDifficulty(@PathVariable String difficulty, Model model) {
        model.addAttribute("workouts", workoutService.findWorkoutsByDifficulty(difficulty));
        model.addAttribute("filterDifficulty", difficulty);
        model.addAttribute("workout", new Workout());
        return "workouts/index";
    }
    
    // Только завершенные тренировки
    @GetMapping("/completed")
    public String getCompletedWorkouts(Model model) {
        model.addAttribute("workouts", workoutService.getCompletedWorkouts());
        model.addAttribute("filter", "completed");
        model.addAttribute("workout", new Workout());
        return "workouts/index";
    }
    
    // Только ожидающие тренировки
    @GetMapping("/pending")
    public String getPendingWorkouts(Model model) {
        model.addAttribute("workouts", workoutService.getPendingWorkouts());
        model.addAttribute("filter", "pending");
        model.addAttribute("workout", new Workout());
        return "workouts/index";
    }
    
    // Страница статистики
    @GetMapping("/stats")
    public String getStats(Model model) {
        model.addAttribute("stats", workoutService.getWorkoutStats());
        return "workouts/stats";
    }
    
    // Очистка всех тренировок
    @GetMapping("/clear")
    public String clearAllWorkouts(@RequestParam(defaultValue = "false") boolean confirm) {
        if (confirm) {
            workoutService.clearAllWorkouts();
        }
        return "redirect:/fitness/workouts";
    }
    
    // Страница подтверждения очистки
    @GetMapping("/confirm-clear")
    public String confirmClear(Model model) {
        model.addAttribute("workoutCount", workoutService.getWorkoutCount());
        return "workouts/confirm-clear";
    }
}