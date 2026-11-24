package ru.belgu.cad.lab5.dto;

import java.util.List;

/** Сведения о текущем пользователе: логин и роли (для клиентского приложения). */
public record UserInfo(String username, List<String> roles) {
}
