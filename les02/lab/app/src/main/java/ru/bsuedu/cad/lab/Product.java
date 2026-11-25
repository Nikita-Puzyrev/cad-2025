package ru.bsuedu.cad.lab;

public record Product(
        int productId,
        String name,
        String description,
        int categoryId,
        double price,
        int stockQuantity,
        String imageUrl,
        String createdAt,
        String updatedAt
) {

}