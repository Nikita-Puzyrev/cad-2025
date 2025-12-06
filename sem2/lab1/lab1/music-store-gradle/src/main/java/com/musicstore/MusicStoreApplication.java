package com.musicstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicStoreApplication.class, args);
        System.out.println("Приложение успешно запущено!");
        System.out.println("Доступно по адресу: http://localhost:8080");
        System.out.println("API: http://localhost:8080/api/equipment/");
    }
}