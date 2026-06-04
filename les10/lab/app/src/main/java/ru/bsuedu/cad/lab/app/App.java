package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.Config;

public class App {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(Config.class)) {
            ShopApplication application = context.getBean(ShopApplication.class);
            application.run();
        }
    }
}