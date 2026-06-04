# Отчет о лабораторной работе №4

## Цель работы

Целью лабораторной работы является добавление в приложение возможности сохранения данных в базу данных, выполнение SQL-запросов и вывод результатов выполнения запросов в логи.

В ходе работы необходимо подключить встроенную базу данных H2, настроить работу с ней через Spring JDBC, создать таблицы `PRODUCTS` и `CATEGORIES`, сохранить в них данные из CSV-файлов, а также выполнить запрос к базе данных с использованием `JdbcTemplate` и `RowMapper`.

## Выполнение работы

Первым делом был скопирован результат выполнения лабораторной работы №2 в директорию `/les06/lab/`.

Для работы с базой данных в проект были добавлены зависимости `spring-jdbc`, `h2`, `slf4j-api` и `logback-classic`.

Фрагмент файла `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-aop:6.2.2")
    implementation("org.springframework:spring-jdbc:6.2.2")

    implementation("org.aspectj:aspectjweaver:1.9.22.1")

    implementation("com.h2database:h2:2.3.232")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

Для хранения настроек приложения используется файл `application.properties`.

```properties
product.file=product.csv
category.file=category.csv
html.output-file=products.html
```

В данном файле задаются имена CSV-файлов с данными о продуктах и категориях.

Для подключения встроенной базы данных H2 был изменен класс `Config.java`.

```java
package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class Config {

    @Bean(destroyMethod = "shutdown")
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

В данном классе создается встроенная база данных H2. Метод `addScript("classpath:schema.sql")` указывает, что при запуске приложения должен быть выполнен SQL-скрипт `schema.sql`, который создает необходимые таблицы.

Также создается бин `JdbcTemplate`, который используется для выполнения SQL-запросов к базе данных.

Для создания таблиц `CATEGORIES` и `PRODUCTS` был создан файл `schema.sql` в директории `src/main/resources`.

```sql
CREATE TABLE CATEGORIES (
    category_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000)
);

CREATE TABLE PRODUCTS (
    product_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    category_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL,
    image_url VARCHAR(1000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_products_categories
        FOREIGN KEY (category_id)
        REFERENCES CATEGORIES(category_id)
);
```

В таблице `CATEGORIES` хранится информация о категориях товаров.
В таблице `PRODUCTS` хранится информация о товарах.
Поле `category_id` в таблице `PRODUCTS` является внешним ключом и связано с полем `category_id` таблицы `CATEGORIES`.

Для представления категории был создан класс `Category`.

```java
package ru.bsuedu.cad.lab;

public class Category {
    private int categoryID;
    private String name;
    private String description;

    public Category(int categoryID, String name, String description) {
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
```

Для предоставления списка категорий был создан интерфейс `CategoryProvider`.

```java
package ru.bsuedu.cad.lab;

import java.util.List;

public interface CategoryProvider {
    List<Category> getCategories();
}
```

Для разбора CSV-файла с категориями был создан класс `CategoryCSVParser`.

```java
package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryCSVParser {

    public List<Category> parse(String text) {
        List<Category> categories = new ArrayList<>();

        String[] lines = text.split("\\R");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] params = line.split(",", -1);

            categories.add(new Category(
                    Integer.parseInt(params[0].trim()),
                    params[1].trim(),
                    params[2].trim()
            ));
        }

        return categories;
    }
}
```

Для получения данных из файла `category.csv` был реализован класс `ConcreteCategoryProvider`.

```java
package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    private final Reader reader;
    private final CategoryCSVParser parser;
    private final String fileName;

    public ConcreteCategoryProvider(
            Reader reader,
            CategoryCSVParser parser,
            @Value("#{environment['category.file']}") String fileName
    ) {
        this.reader = reader;
        this.parser = parser;
        this.fileName = fileName;
    }

    @Override
    public List<Category> getCategories() {
        return parser.parse(reader.read(fileName));
    }
}
```

Имя файла с категориями внедряется из `application.properties` с помощью аннотации `@Value`.

Также был изменен интерфейс `Reader`, чтобы один и тот же класс чтения ресурсов мог читать разные CSV-файлы.

```java
package ru.bsuedu.cad.lab;

public interface Reader {
    String read(String fileName);
}
```

Реализация `ResourceFileReader` читает файл из ресурсов приложения.

```java
package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader, InitializingBean {

    @Override
    public String read(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);

        try (var inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + fileName, e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Bean ResourceFileReader полностью инициализирован: " + LocalDateTime.now());
    }
}
```

Для сохранения данных из CSV-файлов в базу данных была добавлена новая реализация интерфейса `Renderer` — класс `DataBaseRenderer`.

```java
package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRequest categoryRequest;

    public DataBaseRenderer(
            ProductProvider productProvider,
            CategoryProvider categoryProvider,
            JdbcTemplate jdbcTemplate,
            CategoryRequest categoryRequest
    ) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRequest = categoryRequest;
    }

    @Override
    public void render() {
        saveCategories();
        saveProducts();

        categoryRequest.printCategoriesWithMoreThanOneProduct();
    }

    private void saveCategories() {
        var categories = categoryProvider.getCategories();

        for (Category category : categories) {
            jdbcTemplate.update(
                    """
                    INSERT INTO CATEGORIES(category_id, name, description)
                    VALUES (?, ?, ?)
                    """,
                    category.getCategoryID(),
                    category.getName(),
                    category.getDescription()
            );
        }

        System.out.println("Категории успешно сохранены в базу данных");
    }

    private void saveProducts() {
        var products = productProvider.getProducts();

        for (Product product : products) {
            jdbcTemplate.update(
                    """
                    INSERT INTO PRODUCTS(
                        product_id,
                        name,
                        description,
                        category_id,
                        price,
                        stock_quantity,
                        image_url,
                        created_at,
                        updated_at
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    product.getProductID(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryID(),
                    product.getPrice(),
                    product.getStock_quantity(),
                    product.getImageURL(),
                    new Timestamp(product.getCreatedAt().getTime()),
                    new Timestamp(product.getUpdatedAt().getTime())
            );
        }

        System.out.println("Продукты успешно сохранены в базу данных");
    }
}
```

Аннотация `@Primary` указывает Spring, что при запросе бина типа `Renderer` по умолчанию нужно использовать именно `DataBaseRenderer`.

В классе `App.java` получение реализации `Renderer` выполняется по типу.

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

Так как `DataBaseRenderer` помечен аннотацией `@Primary`, при запуске приложения используется именно он.

Для выполнения SQL-запроса к базе данных был реализован класс `CategoryRequest`.

```java
package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {

    private static final Logger log = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;
    private final CategoryProductCountRowMapper rowMapper;

    public CategoryRequest(
            JdbcTemplate jdbcTemplate,
            CategoryProductCountRowMapper rowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public void printCategoriesWithMoreThanOneProduct() {
        String sql = """
                SELECT
                    c.category_id,
                    c.name AS category_name,
                    COUNT(p.product_id) AS product_count
                FROM CATEGORIES c
                JOIN PRODUCTS p ON c.category_id = p.category_id
                GROUP BY c.category_id, c.name
                HAVING COUNT(p.product_id) > 1
                """;

        var result = jdbcTemplate.query(sql, rowMapper);

        for (CategoryProductCount item : result) {
            log.info(
                    "Категория: id={}, название='{}', количество товаров={}",
                    item.getCategoryID(),
                    item.getCategoryName(),
                    item.getProductCount()
            );
        }
    }
}
```

Данный класс выполняет SQL-запрос, который получает список категорий, количество товаров в которых больше единицы.

Для хранения результата запроса был создан класс `CategoryProductCount`.

```java
package ru.bsuedu.cad.lab;

public class CategoryProductCount {
    private final int categoryID;
    private final String categoryName;
    private final int productCount;

    public CategoryProductCount(int categoryID, String categoryName, int productCount) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.productCount = productCount;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getProductCount() {
        return productCount;
    }
}
```

Для преобразования строк результата SQL-запроса в Java-объекты был создан класс `CategoryProductCountRowMapper`.

```java
package ru.bsuedu.cad.lab;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryProductCountRowMapper implements RowMapper<CategoryProductCount> {

    @Override
    public CategoryProductCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CategoryProductCount(
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getInt("product_count")
        );
    }
}
```

Для настройки логирования был добавлен файл `logback.xml`.

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

После запуска приложения командой:

```bash
gradle :app:clean :app:run --rerun-tasks
```

приложение успешно подключилось к встроенной базе данных H2, создало таблицы, сохранило категории и продукты, а затем выполнило SQL-запрос.

Пример вывода приложения:

```text
16:40:08 INFO  o.s.j.d.e.EmbeddedDatabaseFactory - Starting embedded database: url='jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'
Bean ResourceFileReader полностью инициализирован: 2026-06-04T16:40:09.012839100
Категории успешно сохранены в базу данных
Время парсинга CSV-файла: 24.5379 мс
Продукты успешно сохранены в базу данных
16:40:09 INFO  ru.bsuedu.cad.lab.CategoryRequest - Категория: id=5, название='Средства ухода', количество товаров=2
```
## Выводы

В ходе выполнения лабораторной работы была добавлена возможность сохранения данных в базу данных.

Была подключена встроенная база данных H2 и настроен `EmbeddedDatabaseBuilder`, который при запуске приложения выполняет SQL-скрипт `schema.sql` и создает таблицы `CATEGORIES` и `PRODUCTS`.

Для работы с базой данных был использован `JdbcTemplate`. С его помощью были выполнены операции вставки данных из CSV-файлов в таблицы базы данных.

Также был реализован класс `CategoryRequest`, который выполняет SQL-запрос для получения списка категорий, количество товаров в которых больше единицы. Для преобразования результата запроса в Java-объекты был использован `RowMapper`.

Результаты выполнения запроса выводятся в консоль с помощью библиотеки логирования Logback на уровне `INFO`.
