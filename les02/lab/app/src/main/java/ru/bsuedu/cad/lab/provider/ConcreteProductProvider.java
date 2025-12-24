package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.parser.Parser;
import ru.bsuedu.cad.lab.reader.Reader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс, который:
 * 1. Читает CSV-файл через Reader
 * 2. Парсит CSV через Parser
 * 3. Возвращает список Product
 */
@Component
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String csv = reader.read();
        return parser.parse(csv);
    }
}
