package ru.belgu.cad.lab5.dto;

/** Тело запроса на создание задачи из клиентского приложения. */
public record NewTaskRequest(String title, String description,
                             String category, String priority) {
}
