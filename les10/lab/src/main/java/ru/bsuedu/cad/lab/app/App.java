package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.service.DataLoaderService;
import ru.bsuedu.cad.lab.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);

        DataLoaderService loader = ctx.getBean(DataLoaderService.class);
        OrderService orderService = ctx.getBean(OrderService.class);

        // загрузка CSV
        loader.loadAll();

        // получение данных из БД через сервис (транзакция)
        Customer customer = orderService.getCustomerById(1);
        Product product = orderService.getProductById(1);

        // создание заказа
        Order order = new Order();
        order.setId(1);
        order.setCustomer(customer);
        order.setStatus("NEW");
        order.setShippingAddress(customer.getAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(product.getPrice());

        // позиция заказа
        OrderDetail detail = new OrderDetail();
        detail.setId(1);
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(1);
        detail.setPrice(product.getPrice());

        order.getDetails().add(detail);

        // сохранение
        orderService.createOrder(order);

        // проверка
        List<Order> orders = orderService.getAllOrders();

        System.out.println("===================================");
        System.out.println("Заказов в базе: " + orders.size());
        System.out.println("Клиент: " + orders.get(0).getCustomer().getName());
        System.out.println("Товар: " +
                orders.get(0).getDetails().get(0).getProduct().getName());
        System.out.println("===================================");

        ctx.close();
    }
}