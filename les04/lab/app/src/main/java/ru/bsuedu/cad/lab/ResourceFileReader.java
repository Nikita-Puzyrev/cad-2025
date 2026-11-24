package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader, InitializingBean {

    private final String filePath;

    public ResourceFileReader(@Value("#{environment['product.file']}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String read() {
        ClassPathResource resource = new ClassPathResource(filePath);

        try (var inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + filePath, e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Bean ResourceFileReader полностью инициализирован: " + LocalDateTime.now());
    }
}