package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Primary
@Component
public class DataBaseRenderer implements Renderer {

    private final JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render(List<Product> products, List<Category> categories) {

        String insertCat = "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)";
        for (Category c : categories) {
            jdbcTemplate.update(insertCat, c.categoryId(), c.name(), c.description());
        }


        String insertProd = "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (Product p : products) {
            jdbcTemplate.update(insertProd, p.productId(), p.name(), p.description(),
                    p.categoryId(), p.price(), p.stockQuantity(), p.imageUrl(),
                    p.createdAt(), p.updatedAt());
        }
        System.out.println("Данные успешно сохранены в базу данных H2!");
    }
}