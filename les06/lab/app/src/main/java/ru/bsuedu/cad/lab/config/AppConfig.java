package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Конфигурация Spring.
 * Говорит Spring: "ищи классы с @Component в пакете ru.bsuedu.cad.lab".
 */
@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@PropertySource("classpath:application.properties")
public class AppConfig {
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
