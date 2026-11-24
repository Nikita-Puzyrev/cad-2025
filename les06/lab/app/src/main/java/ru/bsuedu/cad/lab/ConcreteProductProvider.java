package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;
    private final String fileName;

    public ConcreteProductProvider(
            Reader reader,
            Parser parser,
            @Value("#{environment['product.file']}") String fileName
    ) {
        this.reader = reader;
        this.parser = parser;
        this.fileName = fileName;
    }

    @Override
    public List<Product> getProducts() {
        return parser.parse(reader.read(fileName));
    }
}