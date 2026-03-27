package ru.bsuedu.cad.lab.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class ResourceFileReader implements Reader {

    @Value("${products.file.name}")
    private String fileName;

    // Сообщение при инициализации бина
    @PostConstruct
    public void onInit() {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("[ResourceFileReader] Bean initialized at: " + time);
        System.out.println("[ResourceFileReader] Default CSV file: " + fileName);
    }

    // Чтение файла по умолчанию (products.csv)
    @Override
    public String read() {
        return readFile(fileName);
    }

    // Универсальное чтение любого файла
    public String readFile(String name) {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(name);

        if (is == null) {
            throw new RuntimeException("Файл не найден: " + name);
        }

        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();

        return text;
    }
}
