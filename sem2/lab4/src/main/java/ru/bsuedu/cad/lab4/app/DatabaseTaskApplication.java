package ru.bsuedu.cad.lab4.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.bsuedu.cad.lab4.persistence.TaskDatabase;

@SpringBootApplication
@Import(TaskDatabase.class)
public class DatabaseTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatabaseTaskApplication.class, args);
    }
}
