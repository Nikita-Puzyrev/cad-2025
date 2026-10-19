
package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.util.Date;

public class Product {

    public final long productId;
    public final String name;
    public final String description;
    public final int categoryId;
    public final BigDecimal price;
    public final int stockQuantity;
    public final String imageUrl;
    public final Date createdAt;
    public final Date updatedAt;

    public Product(
            long productId,
            String name,
            String description,
            int categoryId,
            BigDecimal price,
            int stockQuantity,
            String imageUrl,
            Date createdAt,
            Date updatedAt) {

        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
