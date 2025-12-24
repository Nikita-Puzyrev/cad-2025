package ru.bsuedu.cad.lab.reader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.stereotype.Component;

/**
 * Реализация Reader, которая читает файл products.csv из ресурсов.
 */
@Component  // Позволяет Spring найти и создать этот объект
public class ResourceFileReader implements Reader {

    // Имя файла в папке resources
    private final String resourceName = "/products.csv";

    @Override
    public String read() {
        // Открываем файл как поток
        InputStream is = getClass().getResourceAsStream(resourceName);

        if (is == null) return ""; // Если файла нет — пустая строка

        // Читаем файл построчно
        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();

        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine()).append("\n");
        }

        scanner.close();
        return sb.toString();
    }
}
