package ru.belgu.client;

public record TaskItem(long id, String description, boolean completed, int priority, String createdAt) {
}
