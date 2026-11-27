package ru.bsuedu.cad.lab4.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import ru.bsuedu.cad.lab2.TaskController;
import ru.bsuedu.cad.lab3.SecurityConfiguration;

@Configuration(proxyBeanMethods = false)
@EntityScan
@EnableJpaRepositories
@EnableMethodSecurity
@PropertySource("classpath:tasks-database.properties")
@Import({TaskController.class, SecurityConfiguration.class, DatabaseTaskStore.class, DatabaseUsers.class, TaskDetailsController.class})
public class TaskDatabase {
}
