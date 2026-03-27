package ru.bsuedu.cad.lab.renderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ConcreteCategoryProvider;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import org.springframework.context.annotation.Primary;
import java.util.List;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final ConcreteCategoryProvider categoryProvider;
    private final JdbcTemplate jdbc;

    @Autowired
    public DataBaseRenderer(
            ProductProvider productProvider,
            ConcreteCategoryProvider categoryProvider,
            JdbcTemplate jdbc) {

        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbc = jdbc;
    }

    @Override
    public void render() {

        List<Category> categories = categoryProvider.getCategories();
        List<Product> products = productProvider.getProducts();

        for (Category c : categories) {
            jdbc.update("""
                INSERT INTO CATEGORIES (category_id, name, description)
                VALUES (?,?,?)
            """,
                    c.getCategoryId(),
                    c.getName(),
                    c.getDescription());
        }

        for (Product p : products) {
            jdbc.update("""
                INSERT INTO PRODUCTS
                (product_id, name, description, category_id,
                 price, stock_quantity, image_url, created_at, updated_at)
                VALUES (?,?,?,?,?,?,?,?,?)
            """,
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getCategoryId(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    p.getImageUrl(),
                    p.getCreatedAt(),
                    p.getUpdatedAt());
        }
    }
}
