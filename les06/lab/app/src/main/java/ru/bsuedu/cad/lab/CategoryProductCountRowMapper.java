package ru.bsuedu.cad.lab;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryProductCountRowMapper implements RowMapper<CategoryProductCount> {

    @Override
    public CategoryProductCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CategoryProductCount(
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getInt("product_count")
        );
    }
}