package ru.bsuedu.cad.lab2;

import java.util.Locale;

public record Task(long id, String title, String description, boolean completed) {
    public boolean matches(String term) {
        return title.toLowerCase(Locale.ROOT).contains(term) || description.toLowerCase(Locale.ROOT).contains(term);
    }
}
