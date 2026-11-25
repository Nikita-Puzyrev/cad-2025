package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.bsuedu.cad.lab.HibernateConfig;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.DataImportService;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@EnableAspectJAutoProxy
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // Добавляем HibernateConfig.class
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(App.class, HibernateConfig.class)) {

            //база данных из CSV
            DataImportService dataImportService = context.getBean(DataImportService.class);
            dataImportService.importAllData();

            // заказ через сервис
            OrderService orderService = context.getBean(OrderService.class);


            log.info("Создаем новый заказ для клиента ID=1...");
            Order newOrder = orderService.createOrder(1, List.of(1, 2));

            // лог
            log.info("Заказ успешно создан! ID заказа: {}, Сумма: {}, Адрес доставки: {}",
                    newOrder.getOrderId(), newOrder.getTotalPrice(), newOrder.getShippingAddress());

            // получаем все заказы из БД
            log.info("--- Список всех заказов в базе данных ---");
            List<Order> allOrders = orderService.getAllOrders();
            for (Order o : allOrders) {
                log.info("Заказ ID: {}, Клиент: {}, Сумма: {}, Статус: {}",
                        o.getOrderId(), o.getCustomer().getName(), o.getTotalPrice(), o.getStatus());
            }
        }
    }
}