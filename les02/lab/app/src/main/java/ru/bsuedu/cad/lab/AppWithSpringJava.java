package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.renderer.Renderer;

/**
 * Главный класс.
 * Запускается командой: gradle run
 */
public class AppWithSpringJava {

    public static void main(String[] args) {

        // Создаем Spring-контейнер и загружаем конфигурацию
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // Получаем рендерер по имени "renderer"
        Renderer renderer = ctx.getBean("renderer", Renderer.class);

        // Запускаем вывод товаров
        renderer.render();
    }
}
