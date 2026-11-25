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
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(App.class)) {

            // Получаем наши бины
            ResourceFileReader reader = context.getBean(ResourceFileReader.class);

            // Так как у нас две реализации Renderer, но HTML помечен @Primary,
            // Spring подставит именно HTMLTableRenderer
            Renderer renderer = context.getBean(Renderer.class);

            // Выполняем работу
            List<Product> products = reader.readProducts();

            try {
                renderer.render(products);
            } catch (Exception e) {
                System.err.println("Ошибка рендеринга: " + e.getMessage());
            }
        }
    }
}