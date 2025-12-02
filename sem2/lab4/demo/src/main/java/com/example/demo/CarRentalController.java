package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class CarRentalController {

    private final CarRentalService carRentalService;

    @Autowired
    public CarRentalController(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    // Главная страница
    @GetMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            model.addAttribute("username", auth.getName());
        }
        return "index";
    }

    // Страница логина
    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/catalog";
        }
        return "login";
    }

    // Каталог автомобилей
    @GetMapping("/catalog")
    public String showCatalog(Model model) {
        model.addAttribute("cars", carRentalService.getAllCars());
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("currentUser", getCurrentUsername());
        return "catalog";
    }

    // Форма аренды
    @GetMapping("/rent/{carId}")
    public String showRentForm(@PathVariable Long carId, Model model) {
        Optional<Car> optionalCar = carRentalService.findCarById(carId);
        if (optionalCar.isPresent()) {
            model.addAttribute("car", optionalCar.get());
            model.addAttribute("rentalRequest", new Rental());
            model.addAttribute("currentUser", getCurrentUsername());
            return "rent-form";
        }
        return "redirect:/catalog";
    }

    // Обработка аренды
    @PostMapping("/rent")
    public String processRent(@ModelAttribute("rentalRequest") Rental rentalRequest,
                              @RequestParam Long carId,
                              @RequestParam String customerName,
                              @RequestParam String customerPhone,
                              @RequestParam String customerEmail,
                              Model model) {
        String userLogin = getCurrentUsername();
        boolean success = carRentalService.rentCar(carId, customerName, customerPhone, customerEmail, userLogin, rentalRequest.getRentalDays());
        if (success) {
            return "redirect:/catalog?success";
        } else {
            return "redirect:/catalog?error";
        }
    }

    // НОВЫЙ МЕТОД: Отмена заказа клиентом
    @PostMapping("/cancel-order/{rentalId}")
    public String cancelOrder(@PathVariable Long rentalId) {
        String userLogin = getCurrentUsername();
        boolean success = carRentalService.cancelRental(rentalId, userLogin);
        if (success) {
            return "redirect:/my-orders?cancelSuccess";
        } else {
            return "redirect:/my-orders?cancelError";
        }
    }

    // Страница со списком ВСЕХ заказов (только для админа)
    @GetMapping("/admin/orders")
    public String showAllOrders(Model model) {
        model.addAttribute("rentals", carRentalService.getAllRentals());
        model.addAttribute("isAdmin", true);
        return "orders";
    }

    // Страница с заказами текущего пользователя
    @GetMapping("/my-orders")
    public String showMyOrders(Model model) {
        String userLogin = getCurrentUsername();
        model.addAttribute("rentals", carRentalService.getUserRentals(userLogin));
        model.addAttribute("isAdmin", false);
        model.addAttribute("username", userLogin);
        model.addAttribute("currentUser", userLogin);
        return "my-orders";
    }

    // Админ-панель
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("cars", carRentalService.getAllCars());
        model.addAttribute("rentals", carRentalService.getActiveRentals());
        return "admin-panel";
    }

    // Добавление нового автомобиля (форма)
    @GetMapping("/admin/add-car")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "add-car-form";
    }

    // Добавление нового автомобиля (обработка)
    @PostMapping("/admin/add-car")
    public String addCar(@ModelAttribute Car car) {
        carRentalService.addCar(car);
        return "redirect:/admin?success";
    }

    // Форма редактирования автомобиля
    @GetMapping("/admin/edit-car/{carId}")
    public String showEditCarForm(@PathVariable Long carId, Model model) {
        Optional<Car> optionalCar = carRentalService.findCarById(carId);
        if (optionalCar.isPresent()) {
            model.addAttribute("car", optionalCar.get());
            return "edit-car-form";
        }
        return "redirect:/admin?error";
    }

    // Обработка редактирования автомобиля
    @PostMapping("/admin/edit-car/{carId}")
    public String editCar(@PathVariable Long carId, @ModelAttribute Car car) {
        boolean success = carRentalService.updateCar(carId, car);
        if (success) {
            return "redirect:/admin?updated";
        } else {
            return "redirect:/admin?error";
        }
    }

    // Удаление автомобиля
    @PostMapping("/admin/delete-car/{carId}")
    public String deleteCar(@PathVariable Long carId) {
        boolean success = carRentalService.deleteCar(carId);
        if (success) {
            return "redirect:/admin?deleted";
        } else {
            return "redirect:/admin?error";
        }
    }

    // Возврат автомобиля (завершение аренды)
    @PostMapping("/admin/return-car/{rentalId}")
    public String returnCar(@PathVariable Long rentalId) {
        carRentalService.returnCar(rentalId);
        return "redirect:/admin?returned";
    }

    // Удаление записи об аренде
    @PostMapping("/admin/delete-rental/{rentalId}")
    public String deleteRental(@PathVariable Long rentalId) {
        boolean success = carRentalService.deleteRental(rentalId);
        if (success) {
            return "redirect:/admin?rentalDeleted";
        } else {
            return "redirect:/admin?error";
        }
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return auth.getName();
        }
        return null;
    }
}