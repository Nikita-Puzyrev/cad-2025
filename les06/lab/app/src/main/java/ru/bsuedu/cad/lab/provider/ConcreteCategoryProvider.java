package ru.bsuedu.cad.lab.provider;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.reader.ResourceFileReader;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;

@Component
public class ConcreteCategoryProvider {

    private final ResourceFileReader reader;

    @Value("${category.file.name}")
    private String categoryFile;

    public ConcreteCategoryProvider(ResourceFileReader reader) {
        this.reader = reader;
    }

    public List<Category> getCategories() {

        String csv = reader.readFile(categoryFile);

        List<Category> list = new ArrayList<>();
        String[] lines = csv.split("\\n");

        for (int i = 1; i < lines.length; i++) {
            String[] p = lines[i].split(",");

            Category c = new Category();
            c.setCategoryId(Integer.parseInt(p[0]));
            c.setName(p[1]);
            c.setDescription(p[2]);

            list.add(c);
        }

        return list;
    }
}

