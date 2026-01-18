package com.example.university.hrm_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Точка входа и основное приложение HRM-системы Университета.
 *
 * Этот класс служит точкой запуска приложения и определяет ключевые элементы
 * инфраструктуры, включая сканирование компонентов, репозиториев и сущностей.
 */
@SpringBootApplication(scanBasePackages = {"com.example.university"})
@ComponentScan(basePackages = {"com.example.university"}) // Подразумевается автосканирование компонентов
@EntityScan(basePackages = {"com.example.university"}) // Автосканирование JPA сущностей
@EnableJpaRepositories(basePackages = {"com.example.university"}) // Включает поддержку JPA репозиториев
public class HrmSystemApplication {

	/**
	 * Главная точка входа для запуска приложения.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(HrmSystemApplication.class, args);
	}
}



/*
package com.example.university.hrm_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrmSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrmSystemApplication.class, args);
	}

}

 */
