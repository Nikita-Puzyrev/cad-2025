package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    private final Reader reader;
    private final CategoryCSVParser parser;
    private final String fileName;

    public ConcreteCategoryProvider(
            Reader reader,
            CategoryCSVParser parser,
            @Value("#{environment['category.file']}") String fileName
    ) {
        this.reader = reader;
        this.parser = parser;
        this.fileName = fileName;
    }

    @Override
    public List<Category> getCategories() {
        return parser.parse(reader.read(fileName));
    }
}