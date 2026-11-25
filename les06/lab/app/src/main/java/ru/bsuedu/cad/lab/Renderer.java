package ru.bsuedu.cad.lab;
import java.util.List;
public interface Renderer {
    void render(List<Product> products, List<Category> categories) throws Exception;
}