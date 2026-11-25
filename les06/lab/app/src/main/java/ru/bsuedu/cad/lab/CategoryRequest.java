package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {
    private static final Logger log = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void executeAndLog() {
        log.info("--- Категории с количеством товаров > 1 ---");
        String sql = "SELECT c.name, COUNT(p.product_id) as cnt " +
                "FROM CATEGORIES c JOIN PRODUCTS p ON c.category_id = p.category_id " +
                "GROUP BY c.category_id, c.name HAVING COUNT(p.product_id) > 1";

        jdbcTemplate.query(sql, (rs, rowNum) ->
                "Категория: " + rs.getString("name") + " | Товаров: " + rs.getInt("cnt")
        ).forEach(log::info);

        log.info("--------------------------------------------");
    }
}