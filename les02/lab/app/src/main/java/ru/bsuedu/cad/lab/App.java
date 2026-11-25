package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
class AppConfig {}

public class App {
    public static void main(String[] args) {
        // Инициализируем Spring
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Получаем сервис
            ProductService productService = context.getBean(ProductService.class);

            // Получаем список из CSV
            List<Product> products = productService.getAllProducts();

            //  таблица
            System.out.println("---------------------------------------------------------------------------------------");
            System.out.printf("| %-3s | %-25s | %-4s | %-10s | %-6s |%n", "ID", "Название", "Кат.", "Цена", "Кол-во");
            System.out.println("---------------------------------------------------------------------------------------");

            for (Product p : products) {

                String shortName = p.name();
                if (shortName.length() > 22) {
                    shortName = shortName.substring(0, 19) + "...";
                }

                System.out.printf("| %-3d | %-25s | %-4d | %10.2f | %-6d |%n",
                        p.productId(), shortName, p.categoryId(), p.price(), p.stockQuantity());
            }

            System.out.println("---------------------------------------------------------------------------------------");
        }
    }
}