
package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CSVParser implements Parser {

    @Override
    public List<Product> parse(List<String> lines) {
        List<Product> products = new ArrayList<>();

        // Пропускаем заголовок CSV-файла
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            String[] values = line.split(",", -1);

            if (values.length != 9) {
                throw new IllegalArgumentException(
                    "Неверное количество полей в строке " + (i + 1)
                );
            }

            Product product = new Product(
                Long.parseLong(values[0].trim()),
                values[1].trim(),
                values[2].trim(),
                Integer.parseInt(values[3].trim()),
                new BigDecimal(values[4].trim()),
                Integer.parseInt(values[5].trim()),
                values[6].trim(),
                Date.valueOf(values[7].trim()),
                Date.valueOf(values[8].trim())
            );

            products.add(product);
        }

        return products;
    }
}
