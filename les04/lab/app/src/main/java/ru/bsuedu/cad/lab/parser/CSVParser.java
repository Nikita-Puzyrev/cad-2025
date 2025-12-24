package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * CSVParser — превращает текст CSV-файла в объекты Product.
 */
@Component
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String csv) {
        List<Product> products = new ArrayList<>();
        String[] lines = csv.split("\\n");

        for (int i = 1; i < lines.length; i++) {
            String[] p = lines[i].split(",");

            Product pr = new Product();
            pr.productId = Long.parseLong(p[0].trim());
            pr.name = p[1].trim();
            pr.description = p[2].trim();
            pr.categoryId = Integer.parseInt(p[3].trim());
            pr.price = new BigDecimal(p[4].trim());
            pr.stockQuantity = Integer.parseInt(p[5].trim());
            pr.imageUrl = p[6].trim();
            pr.createdAt = new Date();
            pr.updatedAt = new Date();

            products.add(pr);  // <-- исправлено
        }
        return products; // <-- исправлено
    }
}
