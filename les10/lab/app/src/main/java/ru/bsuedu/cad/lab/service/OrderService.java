package ru.bsuedu.cad.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Integer customerId, Map<Integer, Integer> products) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден: " + customerId));

        Order order = new Order(
                customer,
                LocalDateTime.now(),
                "CREATED",
                "Белгород"
        );

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Integer productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар не найден: " + productId));

            BigDecimal detailPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

            OrderDetail detail = new OrderDetail(product, quantity, detailPrice);
            order.addDetail(detail);

            totalPrice = totalPrice.add(detailPrice);
        }

        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        log.info(
                "Создан заказ: id={}, покупатель='{}', сумма={}, позиций={}",
                savedOrder.getOrderId(),
                savedOrder.getCustomer().getName(),
                savedOrder.getTotalPrice(),
                savedOrder.getDetails().size()
        );

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}