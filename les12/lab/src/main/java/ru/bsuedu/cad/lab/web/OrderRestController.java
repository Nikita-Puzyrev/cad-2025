package ru.bsuedu.cad.lab.web;

import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService service;

    public OrderRestController(OrderService service) {
        this.service = service;
    }

    // 🔹 Получить список заказов
    @GetMapping
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }

    // 🔹 Получить заказ по ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable("id") Integer id) {
        return service.getOrderById(id);
    }

    // 🔹 Создать новый заказ
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }

    // 🔹 Обновить заказ
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable("id") Integer id,
                             @RequestBody Order order) {
        return service.updateOrder(id, order);
    }

    // 🔹 Удалить заказ
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable("id") Integer id) {
        service.deleteOrder(id);
    }
}