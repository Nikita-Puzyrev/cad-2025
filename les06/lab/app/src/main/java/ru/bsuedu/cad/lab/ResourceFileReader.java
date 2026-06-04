package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader, InitializingBean {

    @Override
    public String read(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);

        try (var inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + fileName, e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Bean ResourceFileReader полностью инициализирован: " + LocalDateTime.now());
    }
}