package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider provider;

    @Autowired
    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        if (products.isEmpty()) {
            System.out.println("Нет данных для генерации HTML.");
            return;
        }

        try (FileWriter writer = new FileWriter("products.html")) {

            writer.write("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Products</title>
                    <style>
                        table {
                            border-collapse: collapse;
                            width: 100%;
                        }
                        th, td {
                            border: 1px solid black;
                            padding: 8px;
                            text-align: left;
                        }
                        th {
                            background-color: #f2f2f2;
                        }
                    </style>
                </head>
                <body>
                <h2>Product List</h2>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Stock</th>
                    </tr>
                """);

            for (Product p : products) {
                writer.write(String.format("""
                    <tr>
                        <td>%d</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%d</td>
                    </tr>
                    """,
                        p.getProductId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStockQuantity()
                ));
            }

            writer.write("""
                </table>
                </body>
                </html>
                """);

            System.out.println("HTML-файл успешно создан: products.html");

        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи HTML-файла", e);
        }
    }
}
