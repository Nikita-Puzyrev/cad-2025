package ru.belgu.cad.lab4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Лабораторная работа №4. Взаимодействие Spring и PostgreSQL.
 * Список задач хранится в базе данных; расширенная схема включает подзадачи,
 * пользователей, историю входов и историю изменений задач.
 */
@SpringBootApplication
public class Lab4Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab4Application.class, args);
    }
}
