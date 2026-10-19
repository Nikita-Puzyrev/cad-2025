
package ru.bsuedu.cad.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourceFileReader implements Reader {

    private final String fileName;

    public ResourceFileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> read() {
        InputStream stream = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (stream == null) {
            throw new IllegalArgumentException(
                    "Файл не найден: " + fileName);
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     stream,
                                     StandardCharsets.UTF_8))) {

            return reader.lines().toList();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Ошибка чтения файла: " + fileName, e);
        }
    }
}
