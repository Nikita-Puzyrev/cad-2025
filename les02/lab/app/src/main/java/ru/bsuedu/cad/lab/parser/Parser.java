package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;
import java.util.List;

/**
 * Интерфейс Parser — определяет, что любой парсер должен уметь
 * превращать текст в список товаров.
 */
public interface Parser {
    List<Product> parse(String input);
}
