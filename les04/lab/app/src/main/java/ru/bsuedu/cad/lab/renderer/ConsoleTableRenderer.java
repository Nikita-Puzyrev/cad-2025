package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.text.SimpleDateFormat;
import java.util.List;

//@Component
public class ConsoleTableRenderer implements Renderer {

    private final ProductProvider provider;

    @Autowired
    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        if (products.isEmpty()) {
            System.out.println("Нет данных для отображения.");
            return;
        }

        final int ID_W = 6;
        final int NAME_W = 20;
        final int PRICE_W = 12;
        final int STOCK_W = 8;

        String format = "| %-" + ID_W + "s | %-" + NAME_W + "s | %-" + PRICE_W + "s | %-" + STOCK_W + "s |%n";

        printLine(ID_W, NAME_W, PRICE_W, STOCK_W);
        System.out.printf(format, "ID", "Name", "Price", "Stock");
        printLine(ID_W, NAME_W, PRICE_W, STOCK_W);

        for (Product p : products) {
            System.out.printf(
                    format,
                    p.getProductId(),
                    truncate(p.getName(), NAME_W),
                    p.getPrice(),
                    p.getStockQuantity()
            );
        }

        printLine(ID_W, NAME_W, PRICE_W, STOCK_W);
    }

    private void printLine(int... widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            sb.append("-".repeat(w + 2)).append("+");
        }
        System.out.println(sb);
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }
}

