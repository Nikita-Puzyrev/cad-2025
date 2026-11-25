package ru.bsuedu.cad.lab;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceFileReader {

    private final Resource csvFile;

    // Используем SpEL: оператор + склеивает строку "classpath:" с именем файла из проперти
    public ResourceFileReader(@Value("classpath:#{'${product.file.name}'}") Resource csvFile) {
        this.csvFile = csvFile;
    }

    // Вызывается, когда бин полностью инициализирован
    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader полностью инициализирован в: " + LocalDateTime.now());
    }

    public List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 9) {
                    products.add(new Product(
                            Integer.parseInt(parts[0].trim()),
                            parts[1].replace("\"", "").trim(),
                            parts[2].replace("\"", "").trim(),
                            Integer.parseInt(parts[3].trim()),
                            Double.parseDouble(parts[4].trim()),
                            Integer.parseInt(parts[5].trim()),
                            parts[6].trim(), parts[7].trim(), parts[8].trim()
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
        return products;
    }
}