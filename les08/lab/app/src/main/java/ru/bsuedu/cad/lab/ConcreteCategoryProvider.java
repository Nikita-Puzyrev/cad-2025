package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider { // <--- ОБЯЗАТЕЛЬНО

    private final Resource csvFile;

    public ConcreteCategoryProvider(@Value("classpath:category.csv") Resource csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line; boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    categories.add(new Category(
                            Integer.parseInt(parts[0].trim()),
                            parts[1].trim(),
                            parts[2].trim()
                    ));
                }
            }
        } catch (Exception e) { System.err.println("Ошибка чтения категорий: " + e.getMessage()); }
        return categories;
    }
}