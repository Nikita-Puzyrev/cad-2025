package ru.bsuedu.cad.lab3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.bsuedu.cad.lab2.MemoryTaskStore;
import ru.bsuedu.cad.lab2.TaskController;

@SpringBootApplication
@Import({TaskController.class, MemoryTaskStore.class})
public class SecureTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecureTaskApplication.class, args);
    }
}
