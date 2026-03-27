package ru.bsuedu.cad.lab.renderer;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("renderer")
public class ConsoleTableRenderer implements Renderer {

    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        if (products == null || products.isEmpty()) {
            System.out.println("Нет данных для отображения.");
            return;
        }

        // Заголовки
        List<String> headers = Arrays.asList(
                "Product ID",
                "Name",
                "Description",
                "Category ID",
                "Price",
                "Stock",
                "Image URL",
                "Created At",
                "Updated At"
        );

        // Сформировать строки данных
        List<List<String>> rows = new ArrayList<>();
        for (Product p : products) {
            rows.add(Arrays.asList(
                    String.valueOf(p.getProductId()),
                    safe(p.getName()),
                    safe(p.getDescription()),
                    String.valueOf(p.getCategoryId()),
                    (p.getPrice() != null ? p.getPrice().toString() : ""),
                    String.valueOf(p.getStockQuantity()),
                    safe(p.getImageUrl()),
                    (p.getCreatedAt() != null ? p.getCreatedAt().toString() : ""),
                    (p.getUpdatedAt() != null ? p.getUpdatedAt().toString() : "")
            ));
        }

        printTable(headers, rows);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private void printTable(List<String> headers, List<List<String>> rows) {
        int cols = headers.size();
        int[] widths = new int[cols];

        // Инициализация ширин заголовков
        for (int i = 0; i < cols; i++) {
            widths[i] = headers.get(i).length();
        }

        // Вычисление максимальной ширины для каждой колонки по данным
        for (List<String> row : rows) {
            for (int i = 0; i < cols; i++) {
                String cell = row.get(i);
                if (cell == null) cell = "";
                widths[i] = Math.max(widths[i], cell.length());
            }
        }

        // Построить строку-разделитель вида +----+------+---+
        StringBuilder sepBuilder = new StringBuilder();
        for (int w : widths) {
            sepBuilder.append("+");
            for (int k = 0; k < w + 2; k++) { // +2 для пробелов по краям
                sepBuilder.append("-");
            }
        }
        sepBuilder.append("+");
        String separator = sepBuilder.toString();

        // Печать разделителя
        System.out.println(separator);

        // Печать заголовков
        StringBuilder headerRow = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            headerRow.append("| ")
                    .append(padLeft(headers.get(i), widths[i]))
                    .append(" ");
        }
        headerRow.append("|");
        System.out.println(headerRow.toString());

        // Разделитель после заголовков
        System.out.println(separator);

        // Печать строк таблицы
        for (List<String> row : rows) {
            StringBuilder rowBuilder = new StringBuilder();
            for (int i = 0; i < cols; i++) {
                String cell = row.get(i) == null ? "" : row.get(i);
                rowBuilder.append("| ")
                        .append(padLeft(cell, widths[i]))
                        .append(" ");
            }
            rowBuilder.append("|");
            System.out.println(rowBuilder.toString());
        }

        // Финальный разделитель
        System.out.println(separator);
    }

    private String padLeft(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }
}
