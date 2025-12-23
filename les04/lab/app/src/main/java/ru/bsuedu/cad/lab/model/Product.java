package ru.bsuedu.cad.lab.model;

// Импортируем BigDecimal — это тип для цен (точный, а не с плавающей точкой)
import java.math.BigDecimal;
// Импортируем Date — это тип даты
import java.util.Date;

/**
 * Класс Product — описывает товар.
 * Это просто "мешочек данных".
 */
public class Product {

    // Уникальный номер товара
    public long productId;

    // Название товара
    public String name;

    // Короткое описание
    public String description;

    // Категория товара (например 1 — корм, 2 — игрушки)
    public int categoryId;

    // Цена товара
    public BigDecimal price;

    // Количество на складе
    public int stockQuantity;

    // URL картинки
    public String imageUrl;

    // Когда создано
    public Date createdAt;

    // Когда обновлено
    public Date updatedAt;

    // Конструктор — создание объекта Product
    public Product(long productId, String name, String description, int categoryId,
                   BigDecimal price, int stockQuantity, String imageUrl,
                   Date createdAt, Date updatedAt) {

        // Cохраняем параметры в поля
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

    public Product() {

    }

    // Набор геттеров — получают значения полей
    public long getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public BigDecimal getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getImageUrl() { return imageUrl; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
}
