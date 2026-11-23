package ru.belgu.cad.lab1;

import java.time.LocalDateTime;

/**
 * Сообщение (заметка) с датой создания.
 * Дата нужна для дополнительного задания — выборки сообщений, опубликованных
 * после указанного момента.
 */
public record Message(long id, String text, LocalDateTime createdAt) {
}
