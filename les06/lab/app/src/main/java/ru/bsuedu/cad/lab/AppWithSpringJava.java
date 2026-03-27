package ru.bsuedu.cad.lab;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.renderer.Renderer;
import ru.bsuedu.cad.lab.request.CategoryRequest;

public class AppWithSpringJava {

    public static void main(String[] args) {

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(
                        "ru.bsuedu.cad.lab");

        Renderer renderer = ctx.getBean(Renderer.class);
        renderer.render(); // сохраняем в БД

        CategoryRequest req = ctx.getBean(CategoryRequest.class);
        req.printCategoriesWithManyProducts();
    }
}
