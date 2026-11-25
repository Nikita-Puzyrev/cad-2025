package ru.bsuedu.cad.lab.service;

import ru.bsuedu.cad.lab.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(int customerId, List<Integer> productIds);
    List<Order> getAllOrders();
}