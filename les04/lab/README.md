# Отчет о лабораторной работе №2

# Конфигурирование приложения Spring с помощью аннотаций. Применение АОП для логирования

## Цель работы

Обновить конфигурацию Spring-приложения с использованием аннотаций, добавить возможность вывода таблицы товаров в HTML-файл, а также выполнить замер времени парсинга CSV-файла с помощью аспектно-ориентированного программирования.

## Выполнение работы

Перед выполнением работы был скопирован результат выполнения лабораторной работы №1 в директорию `/les04/lab/`.

В ходе работы приложение было изменено таким образом, чтобы конфигурирование осуществлялось не через ручное создание бинов в классе конфигурации, а с помощью аннотаций Spring.

Для этого классы приложения были помечены аннотацией `@Component`, а зависимости между ними стали внедряться через конструкторы.

Был переписан класс `Config.java`.

```java
package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class Config {
}
```

Аннотация `@ComponentScan` позволяет Spring автоматически находить классы, помеченные аннотациями `@Component`, `@Service`, `@Repository` и другими стереотипными аннотациями.

Аннотация `@PropertySource` подключает конфигурационный файл `application.properties`.

Аннотация `@EnableAspectJAutoProxy` включает поддержку аспектно-ориентированного программирования в Spring.

## Получение имени файла из application.properties

Для хранения конфигурационных параметров был создан файл `application.properties` в директории `src/main/resources`.

```properties
product.file=products.csv
html.output-file=products.html
```

Имя CSV-файла теперь не задается напрямую в коде, а внедряется в бин `ResourceFileReader` с помощью аннотации `@Value` и выражения SpEL.

Фрагмент класса `ResourceFileReader.java`:

```java
@Component
public class ResourceFileReader implements Reader, InitializingBean {

    private final String filePath;

    public ResourceFileReader(@Value("#{environment['product.file']}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String read() {
        ClassPathResource resource = new ClassPathResource(filePath);

        try (var inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + filePath, e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Bean ResourceFileReader полностью инициализирован: " + LocalDateTime.now());
    }
}
```

Таким образом, имя файла для загрузки товаров берется из конфигурационного файла, а не хранится внутри класса.

## Использование событий жизненного цикла бина

Для вывода даты и времени полной инициализации бина `ResourceFileReader` был использован интерфейс `InitializingBean`.

После создания объекта и внедрения всех зависимостей Spring вызывает метод `afterPropertiesSet()`.

```java
@Override
public void afterPropertiesSet() {
    System.out.println("Bean ResourceFileReader полностью инициализирован: " + LocalDateTime.now());
}
```

В результате при запуске приложения в консоль выводится дата и время инициализации бина.

## Добавление HTMLTableRenderer

Была добавлена новая реализация интерфейса `Renderer` — класс `HTMLTableRenderer`.

Данная реализация предназначена для вывода списка товаров не в консоль, а в HTML-файл.

```java
@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final String outputFile;

    public HTMLTableRenderer(
            ProductProvider productProvider,
            @Value("#{environment['html.output-file']}") String outputFile
    ) {
        this.productProvider = productProvider;
        this.outputFile = outputFile;
    }

    @Override
    public void render() {
        var products = productProvider.getProducts();

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Товары магазина</title>
                </head>
                <body>
                    <h1>Товары магазина для животных</h1>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Название</th>
                                <th>Описание</th>
                                <th>Категория</th>
                                <th>Цена</th>
                                <th>Остаток</th>
                                <th>Изображение</th>
                                <th>Создан</th>
                                <th>Обновлён</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        for (Product product : products) {
            html.append("""
                            <tr>
                                <td>%d</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%d</td>
                                <td>%s</td>
                                <td>%d</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                    """.formatted(
                    product.getProductID(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryID(),
                    product.getPrice(),
                    product.getStock_quantity(),
                    product.getImageURL(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            ));
        }

        html.append("""
                        </tbody>
                    </table>
                </body>
                </html>
                """);

        try {
            Files.writeString(Path.of(outputFile), html.toString(), StandardCharsets.UTF_8);
            System.out.println("HTML-файл успешно создан: " + outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи HTML-файла: " + outputFile, e);
        }
    }
}
```

Аннотация `@Primary` указывает Spring, что при выборе реализации интерфейса `Renderer` нужно использовать именно `HTMLTableRenderer`.

Это необходимо, так как в приложении есть две реализации интерфейса:

* `ConsoleTableRenderer`;
* `HTMLTableRenderer`.

## Изменение класса App

Класс `App.java` был изменен таким образом, чтобы получать бин типа `Renderer` из Spring-контейнера.

```java
package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

        Renderer renderer = ctx.getBean(Renderer.class);
        renderer.render();
    }
}
```

Spring самостоятельно выбирает реализацию `Renderer`. Так как `HTMLTableRenderer` помечен аннотацией `@Primary`, вызывается именно он.

## Применение АОП для замера времени парсинга

Для измерения времени выполнения метода парсинга CSV-файла был создан аспект `ParserTimingAspect`.

```java
package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParserTimingAspect {

    @Around("execution(* ru.bsuedu.cad.lab.CSVParser.parse(..))")
    public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.nanoTime();
            long duration = finish - start;

            System.out.println("Время парсинга CSV-файла: " + duration / 1_000_000.0 + " мс");
        }
    }
}
```

Аннотация `@Aspect` указывает, что данный класс является аспектом.

Аннотация `@Around` задает advice, который выполняется до и после вызова метода.

Выражение:

```java
execution(* ru.bsuedu.cad.lab.CSVParser.parse(..))
```

означает, что аспект будет применяться к методу `parse()` класса `CSVParser`.

В результате при запуске приложения в консоль выводится время, затраченное на парсинг CSV-файла.

## Результат работы приложения

После запуска приложения командой:

```bash
gradle run
```

приложение выполняет следующие действия:

1. Spring-контейнер создает и связывает бины приложения.
2. Бин `ResourceFileReader` получает имя CSV-файла из `application.properties`.
3. После инициализации `ResourceFileReader` в консоль выводится дата и время его полной инициализации.
4. Приложение считывает CSV-файл с товарами.
5. Метод `CSVParser.parse()` преобразует данные из CSV в список объектов `Product`.
6. Аспект `ParserTimingAspect` измеряет время выполнения метода парсинга.
7. `HTMLTableRenderer` формирует HTML-таблицу и записывает ее в файл `products.html`.

Пример вывода в консоль:

```text
Bean ResourceFileReader полностью инициализирован: 2026-06-04T...
Время парсинга CSV-файла: 2.5 мс
HTML-файл успешно создан: products.html
```

## Выводы
В ходе выполнения лабораторной работы я обновил конфигурацию приложения при помощи аннотаций, добавить класс реализующий представление данных в виде HTML
и замерил скорость выполнения кода при помощи аспектно-ориентированного программирования (АОП).