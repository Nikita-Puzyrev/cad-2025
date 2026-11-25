package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
// Чтение файла
@Service
public class ProductService {

    private final Resource csvFile;

    // Ищем файл product.csv в папке resources
    public ProductService(@Value("classpath:product.csv") Resource csvFile) {
        this.csvFile = csvFile;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                // Разбиваем строку по запятым, игнорируя те, что внутри кавычек
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (parts.length >= 9) {
                    int productId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].replace("\"", "").trim();
                    String description = parts[2].replace("\"", "").trim();
                    int categoryId = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
                    int stockQuantity = Integer.parseInt(parts[5].trim());
                    String imageUrl = parts[6].trim();
                    String createdAt = parts[7].trim();
                    String updatedAt = parts[8].trim();

                    products.add(new Product(productId, name, description, categoryId, price, stockQuantity, imageUrl, createdAt, updatedAt));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении CSV файла: " + e.getMessage());
        }

        return products;
    }
}