package ru.bsuedu.cad.lab5.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.bsuedu.cad.lab4.persistence.TaskDatabase;

@SpringBootApplication
@Import(TaskDatabase.class)
public class ClientServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientServerApplication.class, args);
    }
}
