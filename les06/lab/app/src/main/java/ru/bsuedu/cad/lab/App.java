package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class App {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class)) {


            ProductProvider productProvider = context.getBean(ProductProvider.class);
            CategoryProvider categoryProvider = context.getBean(CategoryProvider.class);

            List<Product> products = productProvider.getProducts();
            List<Category> categories = categoryProvider.getCategories();


            Renderer renderer = context.getBean(Renderer.class);
            try {
                renderer.render(products, categories);
            } catch (Exception e) {
                e.printStackTrace();
            }


            CategoryRequest request = context.getBean(CategoryRequest.class);
            request.executeAndLog();
        }
    }
}