package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final String outputFile;

    public HTMLTableRenderer(
            ProductProvider productProvider,
            @Value("#{environment['html.output-file']}") String outputFile
    ) {
        this.productProvider = productProvider;
        this.outputFile = outputFile;
    }

    @Override
    public void render() {
        var products = productProvider.getProducts();

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Товары магазина</title>
                    <style>
                        table {
                            border-collapse: collapse;
                            width: 100%;
                            font-family: Arial, sans-serif;
                        }
                        th, td {
                            border: 1px solid #999;
                            padding: 8px;
                            text-align: left;
                        }
                        th {
                            background-color: #eeeeee;
                        }
                    </style>
                </head>
                <body>
                    <h1>Товары магазина для животных</h1>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Название</th>
                                <th>Описание</th>
                                <th>Категория</th>
                                <th>Цена</th>
                                <th>Остаток</th>
                                <th>Изображение</th>
                                <th>Создан</th>
                                <th>Обновлён</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        for (Product product : products) {
            html.append("""
                            <tr>
                                <td>%d</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%d</td>
                                <td>%s</td>
                                <td>%d</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                    """.formatted(
                    product.getProductID(),
                    escape(product.getName()),
                    escape(product.getDescription()),
                    product.getCategoryID(),
                    product.getPrice(),
                    product.getStock_quantity(),
                    escape(product.getImageURL()),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            ));
        }

        html.append("""
                        </tbody>
                    </table>
                </body>
                </html>
                """);

        try {
            Files.writeString(Path.of(outputFile), html.toString(), StandardCharsets.UTF_8);
            System.out.println("HTML-файл успешно создан: " + outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи HTML-файла: " + outputFile, e);
        }
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}