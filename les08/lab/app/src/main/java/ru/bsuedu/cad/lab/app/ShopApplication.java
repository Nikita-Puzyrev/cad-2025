package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.CsvDataLoader;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.Map;

@Component
public class ShopApplication {

    private static final Logger log = LoggerFactory.getLogger(ShopApplication.class);

    private final CsvDataLoader csvDataLoader;
    private final OrderService orderService;

    public ShopApplication(CsvDataLoader csvDataLoader, OrderService orderService) {
        this.csvDataLoader = csvDataLoader;
        this.orderService = orderService;
    }

    public void run() {
        csvDataLoader.loadData();

        Order order = orderService.createOrder(
                1,
                Map.of(
                        1, 2,
                        8, 1
                )
        );

        log.info("Проверка сохранения заказа в базе данных");

        var orders = orderService.getAllOrders();

        for (Order savedOrder : orders) {
            log.info(
                    "Заказ найден в БД: id={}, сумма={}, количество позиций={}",
                    savedOrder.getOrderId(),
                    savedOrder.getTotalPrice(),
                    savedOrder.getDetails().size()
            );
        }
    }
}