package ru.bsuedu.cad.lab;

public class CategoryProductCount {
    private final int categoryID;
    private final String categoryName;
    private final int productCount;

    public CategoryProductCount(int categoryID, String categoryName, int productCount) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.productCount = productCount;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getProductCount() {
        return productCount;
    }
}