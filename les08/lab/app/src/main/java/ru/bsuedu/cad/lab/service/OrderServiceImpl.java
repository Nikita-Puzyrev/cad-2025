package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional // Весь метод выполняется в рамках одной транзакции
    public Order createOrder(int customerId, List<Integer> productIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        Order order = new Order();

        order.setOrderId((int) (System.currentTimeMillis() % 100000));
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress(customer.getAddress());

        List<OrderDetail> details = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Integer productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Продукт не найден: " + productId));

            OrderDetail detail = new OrderDetail();
            detail.setOrderDetailId((int) (System.nanoTime() % 1000000));
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());

            totalPrice = totalPrice.add(product.getPrice());
            details.add(detail);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderDetails(details);

        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAllWithCustomer();
    }
}