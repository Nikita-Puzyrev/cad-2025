package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;
import ru.bsuedu.cad.lab.renderer.Renderer;

@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class AppWithSpringJava {

    // НУЖНО для работы @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppWithSpringJava.class);

        Renderer renderer = ctx.getBean(Renderer.class);
        renderer.render();

    }
}
