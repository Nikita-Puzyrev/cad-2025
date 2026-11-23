package ru.belgu.cad.lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Лабораторная работа №1. Введение в Spring Boot.
 * Приложение хранит заметки в памяти и предоставляет REST-интерфейс для работы с ними.
 */
@SpringBootApplication
public class Lab1Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab1Application.class, args);
    }
}
