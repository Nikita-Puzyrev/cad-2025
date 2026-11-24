package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRequest categoryRequest;

    public DataBaseRenderer(
            ProductProvider productProvider,
            CategoryProvider categoryProvider,
            JdbcTemplate jdbcTemplate,
            CategoryRequest categoryRequest
    ) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRequest = categoryRequest;
    }

    @Override
    public void render() {
        saveCategories();
        saveProducts();

        categoryRequest.printCategoriesWithMoreThanOneProduct();
    }

    private void saveCategories() {
        var categories = categoryProvider.getCategories();

        for (Category category : categories) {
            jdbcTemplate.update(
                    """
                    INSERT INTO CATEGORIES(category_id, name, description)
                    VALUES (?, ?, ?)
                    """,
                    category.getCategoryID(),
                    category.getName(),
                    category.getDescription()
            );
        }

        System.out.println("Категории успешно сохранены в базу данных");
    }

    private void saveProducts() {
        var products = productProvider.getProducts();

        for (Product product : products) {
            jdbcTemplate.update(
                    """
                    INSERT INTO PRODUCTS(
                        product_id,
                        name,
                        description,
                        category_id,
                        price,
                        stock_quantity,
                        image_url,
                        created_at,
                        updated_at
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    product.getProductID(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryID(),
                    product.getPrice(),
                    product.getStock_quantity(),
                    product.getImageURL(),
                    new Timestamp(product.getCreatedAt().getTime()),
                    new Timestamp(product.getUpdatedAt().getTime())
            );
        }

        System.out.println("Продукты успешно сохранены в базу данных");
    }
}