

package ru.bsuedu.cad.lab;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {

    private static final String[] HEADERS = {
        "ID", "Name", "Description",
        "Category", "Price", "Stock",
        "Image URL", "Created", "Updated"
    };

    private static final int[] WIDTHS = {
        3, 19, 24, 8, 6, 5, 23, 10, 10
    };

    @Override
    public void render(List<Product> products) {
        String border = createBorder();

        System.out.println(border);
        printRow(HEADERS);
        System.out.println(border);

        for (Product p : products) {
            printRow(new String[]{
                String.valueOf(p.productId),
                p.name,
                p.description,
                String.valueOf(p.categoryId),
                p.price.toPlainString(),
                String.valueOf(p.stockQuantity),
                p.imageUrl,
                String.valueOf(p.createdAt),
                String.valueOf(p.updatedAt)
            });

            System.out.println(border);
        }
    }

    private String createBorder() {
        StringBuilder border = new StringBuilder("+");

        for (int width : WIDTHS) {
            border.append("-".repeat(width + 2))
                  .append("+");
        }

        return border.toString();
    }

    private void printRow(String[] values) {
        int lines = 1;

        for (int i = 0; i < values.length; i++) {
            int count = (values[i].length()
                    + WIDTHS[i] - 1) / WIDTHS[i];

            lines = Math.max(lines, count);
        }

        for (int line = 0; line < lines; line++) {
            StringBuilder result = new StringBuilder("|");

            for (int i = 0; i < values.length; i++) {
                int start = line * WIDTHS[i];
                String part = "";

                if (start < values[i].length()) {
                    int end = Math.min(
                        start + WIDTHS[i],
                        values[i].length()
                    );

                    part = values[i].substring(start, end);
                }

                result.append(" ")
                      .append(part)
                      .append(" ".repeat(
                          WIDTHS[i] - part.length()
                      ))
                      .append(" |");
            }

            System.out.println(result);
        }
    }
}


