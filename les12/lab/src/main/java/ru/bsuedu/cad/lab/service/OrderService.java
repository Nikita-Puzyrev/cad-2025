package ru.bsuedu.cad.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 🔹 Создание заказа
    @Transactional
    public Order createOrder(Order order) {
        Order saved = orderRepository.save(order);
        log.info("Создан заказ: id={}, статус={}",
                saved.getId(),
                saved.getStatus());
        return saved;
    }

    // 🔹 Получить список заказов
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        // Инициализация LAZY-коллекции
        orders.forEach(o -> {
            if (o.getDetails() != null) {
                o.getDetails().size();
            }
        });

        log.info("Получено заказов из БД: {}", orders.size());
        return orders;
    }

    // 🔹 Получить заказ по ID
    @Transactional(readOnly = true)
    public Order getOrderById(Integer id) {
        Order order = orderRepository.findById(id);

        if (order != null && order.getDetails() != null) {
            order.getDetails().size();
        }

        log.info("Получен заказ по id={}", id);
        return order;
    }

    // 🔹 Обновить заказ
    @Transactional
    public Order updateOrder(Integer id, Order updatedOrder) {

        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new RuntimeException("Заказ не найден");
        }
        order.setStatus(updatedOrder.getStatus());
        order.setShippingAddress(updatedOrder.getShippingAddress());
        order.setTotalPrice(updatedOrder.getTotalPrice());
        orderRepository.save(order);
        order.getDetails().size();
        return order;

    }

    // 🔹 Удалить заказ
    @Transactional
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
        log.info("Удален заказ id={}", id);
    }
}