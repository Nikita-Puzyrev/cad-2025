package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Spring.
 * Говорит Spring: "ищи классы с @Component в пакете ru.bsuedu.cad.lab".
 */
@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
public class AppConfig {
}
