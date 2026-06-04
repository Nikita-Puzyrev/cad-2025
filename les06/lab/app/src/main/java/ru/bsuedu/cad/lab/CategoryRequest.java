package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {

    private static final Logger log = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;
    private final CategoryProductCountRowMapper rowMapper;

    public CategoryRequest(
            JdbcTemplate jdbcTemplate,
            CategoryProductCountRowMapper rowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public void printCategoriesWithMoreThanOneProduct() {
        String sql = """
                SELECT
                    c.category_id,
                    c.name AS category_name,
                    COUNT(p.product_id) AS product_count
                FROM CATEGORIES c
                JOIN PRODUCTS p ON c.category_id = p.category_id
                GROUP BY c.category_id, c.name
                HAVING COUNT(p.product_id) > 1
                """;

        var result = jdbcTemplate.query(sql, rowMapper);

        for (CategoryProductCount item : result) {
            log.info(
                    "Категория: id={}, название='{}', количество товаров={}",
                    item.getCategoryID(),
                    item.getCategoryName(),
                    item.getProductCount()
            );
        }
    }
}