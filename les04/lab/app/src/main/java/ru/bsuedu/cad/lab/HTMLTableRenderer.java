package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Primary
@Component
public class HTMLTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) throws Exception {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Products</title>\n</head>\n<body>\n");
        html.append("<table border='1' style='border-collapse: collapse;'>\n");
        html.append("<tr><th>ID</th><th>Name</th><th>Price</th><th>Stock</th></tr>\n");

        for (Product p : products) {
            html.append(String.format("<tr><td>%d</td><td>%s</td><td>%.2f</td><td>%d</td></tr>\n",
                    p.productId(), p.name(), p.price(), p.stockQuantity()));
        }

        html.append("</table>\n</body>\n</html>");

        // Сохраняем файл в корень проекта
        Files.writeString(Path.of("products.html"), html.toString());
        System.out.println("HTML таблица успешно сохранена в файл products.html");
    }
}