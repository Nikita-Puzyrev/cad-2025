package ru.belgu.cad.lab5.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Задача, получаемая от REST-сервера. Содержит дополнительные данные из базы:
 * время создания, приоритет, категорию, автора и подзадачи.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskItem(Long id, String title, String description, boolean completed,
                       String priority, String category, String createdAt,
                       String author, List<SubTaskItem> subTasks) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SubTaskItem(Long id, String description, boolean completed) {
    }

    /** Подзадачи в виде строки для таблицы: «выполнена/нет — описание». */
    public String subTasksAsText() {
        if (subTasks == null || subTasks.isEmpty()) {
            return "—";
        }
        StringBuilder builder = new StringBuilder();
        for (SubTaskItem subTask : subTasks) {
            if (!builder.isEmpty()) {
                builder.append("; ");
            }
            builder.append(subTask.completed() ? "[x] " : "[ ] ")
                    .append(subTask.description());
        }
        return builder.toString();
    }

    public String statusText() {
        return completed ? "выполнена" : "активна";
    }
}
