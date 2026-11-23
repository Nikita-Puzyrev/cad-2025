package ru.belgu.cad.lab5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Лабораторная работа №5. Разработка клиента.
 * Серверная часть: REST-интерфейс (/api) поверх данных PostgreSQL из
 * лабораторной работы №4; клиент — приложение на Swing (см. модуль client).
 */
@SpringBootApplication
public class Lab5ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab5ServerApplication.class, args);
    }
}
