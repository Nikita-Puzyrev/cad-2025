
package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    // Сохраняем метод для стандартного теста Gradle
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        AppConfig.class)) {

            ProductProvider provider =
                    context.getBean(ProductProvider.class);

            Renderer renderer =
                    context.getBean(Renderer.class);

            renderer.render(provider.getProducts());
        }
    }
}
