package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component
public class ConsoleTableRenderer implements Renderer {

    private final ProductProvider productProvider;

    public ConsoleTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() {
        var products = productProvider.getProducts();

        System.out.printf("%-5s %-35s %-12s %-12s %-10s%n",
                "ID", "Название", "Категория", "Цена", "Остаток");

        System.out.println("-------------------------------------------------------------------------------");

        for (Product product : products) {
            System.out.printf("%-5d %-35s %-12d %-12s %-10d%n",
                    product.getProductID(),
                    product.getName(),
                    product.getCategoryID(),
                    product.getPrice(),
                    product.getStock_quantity());
        }
    }
}