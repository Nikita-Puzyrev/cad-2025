package ru.bsuedu.cad.lab.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CategoryRequest {

    private static final Logger log =
            LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbc;

    public CategoryRequest(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void printCategoriesWithManyProducts() {

        List<Map<String, Object>> rows = jdbc.queryForList(
                """
                SELECT c.name, COUNT(p.product_id) AS cnt
                FROM CATEGORIES c
                JOIN PRODUCTS p ON c.category_id = p.category_id
                GROUP BY c.name
                HAVING COUNT(p.product_id) > 1
                """
        );

        for (Map<String, Object> r : rows) {
            log.info("Категория: {} — товаров: {}",
                    r.get("name"),
                    r.get("cnt"));
        }
    }
}
