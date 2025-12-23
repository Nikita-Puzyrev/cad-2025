package ru.bsuedu.cad.lab.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component

    public class ResourceFileReader implements Reader {

    @Value("#{ '${products.file.name}' }")
    private String fileName;

    // Метод жизненного цикла бина
    @PostConstruct
    public void onInit() {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(
                "[ResourceFileReader] Bean initialized at: " + time
        );
        System.out.println(
                "[ResourceFileReader] CSV file: " + fileName
        );
    }

    @Override
    public String read() {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("products.csv");

        if (is == null) {
            throw new RuntimeException("Файл products.csv не найден!");
        }

        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
        String text = scanner.useDelimiter("\\A").next();

        scanner.close();
        return text;
    }
}
