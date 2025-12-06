package com.musicstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicStoreApplication.class, args);
        System.out.println("Магазин музыкального оборудования ЗАПУЩЕН!");
        System.out.println("Веб-интерфейс: http://localhost:8080/web/");
        System.out.println("REST API: http://localhost:8080/api/equipment");
        System.out.println("Статистика: http://localhost:8080/web/stats");
        System.out.println("Управление оборудованием: http://localhost:8080/web/equipment");
    }
}