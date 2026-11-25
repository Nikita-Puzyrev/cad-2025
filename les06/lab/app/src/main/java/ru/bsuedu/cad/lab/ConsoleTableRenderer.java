package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products, List<Category> categories) { // <-- Добавлен параметр
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