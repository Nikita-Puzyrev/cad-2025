package ru.belgu.cad.lab5.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Представление задачи для REST-клиента: включает дополнительные данные,
 * добавленные в лабораторной работе №4 (время создания, приоритет, категория,
 * автор), а также список подзадач.
 */
public record TaskDto(
        Long id,
        String title,
        String description,
        boolean completed,
        String priority,
        String category,
        String createdAt,
        String author,
        List<SubTaskDto> subTasks) {

    public static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public record SubTaskDto(Long id, String description, boolean completed) {
    }
}
