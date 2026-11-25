# Отчет о лабораторной работе 1. Gradle. Базовое приложение Spring

## Цель работы
Создание каркаса Spring-приложения с использованием Java-конфигурации, освоение инструмента сборки Gradle, реализация загрузчика CSV-файлов
## Выполнение работы
Был создан проект с помощью Gradle, добавлены зависимости.

<img width="583" height="226" alt="Снимок экрана 2026-11-24 204509" src="https://github.com/user-attachments/assets/bc09a4e0-0478-4863-b325-38ab204216af" />

Рисунок 1 - Добавление зависимости org.springframework:spring-context:6.2.2

Выполнена установка фреймворка Spring.

<img width="664" height="300" alt="установка фреймворка спринг" src="https://github.com/user-attachments/assets/bd4bfb3e-d946-4e64-94fc-58756d1073eb" />

Рисунок 2 - Установка фреймворка.

С помощью команды "gradle init --package ru.bsuedu.cad.lab" создали каркас проекта. На рисунке 3 представлена иерархия директорий проекта.

<img width="281" height="307" alt="image" src="https://github.com/user-attachments/assets/e63898ef-c9a9-43b6-bb93-63569613ab53" />

Рисунок 3 - Директории проекта

Для хранения данных перед выводом в таблицу реализован класс Product:
```
package ru.bsuedu.cad.lab;

public record Product(
        int productId,
        String name,
        String description,
        int categoryId,
        double price,
        int stockQuantity,
        String imageUrl,
        String createdAt,
        String updatedAt
) {

}
```
Запуск программы и формирование таблицы происходит в классе App:
```
package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
class AppConfig {}

public class App {
    public static void main(String[] args) {
        // Инициализируем Spring
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Получаем сервис
            ProductService productService = context.getBean(ProductService.class);

            // Получаем список из CSV
            List<Product> products = productService.getAllProducts();

            //  таблица
            System.out.println("---------------------------------------------------------------------------------------");
            System.out.printf("| %-3s | %-25s | %-4s | %-10s | %-6s |%n", "ID", "Название", "Кат.", "Цена", "Кол-во");
            System.out.println("---------------------------------------------------------------------------------------");

            for (Product p : products) {

                String shortName = p.name();
                if (shortName.length() > 22) {
                    shortName = shortName.substring(0, 19) + "...";
                }

                System.out.printf("| %-3d | %-25s | %-4d | %10.2f | %-6d |%n",
                        p.productId(), shortName, p.categoryId(), p.price(), p.stockQuantity());
            }

            System.out.println("---------------------------------------------------------------------------------------");
        }
    }
}
```
На рисунке 4 представлен результат работы программы.

<img width="564" height="344" alt="image" src="https://github.com/user-attachments/assets/af0501e9-3393-4228-9e92-a5ca52c7cdf0" />


## Вывод
Научился собирать Spring-приложения с помощью инструмента Gradle. Реализовал функцию считывания CSV-файлов и формирования  данных в виде таблицы.
