package ru.belgu.cad.lab5.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/** Логин и роли текущего пользователя. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserInfo(String username, List<String> roles) {

    public boolean canDelete() {
        return roles != null
                && (roles.contains("ROLE_MODERATOR") || roles.contains("ROLE_ADMIN"));
    }

    public boolean canCreate() {
        return roles != null
                && (roles.contains("ROLE_USER") || roles.contains("ROLE_MODERATOR")
                || roles.contains("ROLE_ADMIN"));
    }
}
