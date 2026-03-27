package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CSVParser — превращает текст CSV-файла в объекты Product.
 */
@Component
public class CSVParser implements Parser {

    // Формат даты для парсинга
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Product> parse(String input) {
        List<Product> products = new ArrayList<>();

        // Разбиваем файл на строки
        String[] lines = input.split("\\r?\\n");

        for (String line : lines) {

            if (line.trim().isEmpty()) continue; // Пропускаем пустые строки

            // Разбиваем строку по запятым
            String[] c = line.split(",");

            try {
                Product p = new Product(
                        Long.parseLong(c[0]),
                        c[1],
                        c[2],
                        Integer.parseInt(c[3]),
                        new BigDecimal(c[4]),
                        Integer.parseInt(c[5]),
                        c[6],
                        sdf.parse(c[7]),
                        sdf.parse(c[8])
                );
                products.add(p);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return products;
    }
}
