package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryCSVParser {

    public List<Category> parse(String text) {
        List<Category> categories = new ArrayList<>();

        String[] lines = text.split("\\R");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] params = line.split(",", -1);

            categories.add(new Category(
                    Integer.parseInt(params[0].trim()),
                    params[1].trim(),
                    params[2].trim()
            ));
        }

        return categories;
    }
}