# Лабораторная работа №3  

**Тема:** Технологии работы с базами данных.JDBC

---

## Задачи

В рамках лабораторной работы выполнено:

- подключена встраиваемая база данных **H2**
- настроен `EmbeddedDatabaseBuilder`
- создан SQL-скрипт `schema.sql`
- реализованы сущности **Product** и **Category**
- реализованы провайдеры CSV-данных
- реализован `DataBaseRenderer` для записи данных в БД
- реализован `CategoryRequest` для SQL-запроса
- настроено логирование **logback**
- приложение запускается через `gradle run`

---

## Структура проекта


les06/lab/app/src/main/java/ru/bsuedu/cad/lab

config/ — конфигурация Spring и БД
model/ — сущности Product, Category
reader/ — чтение CSV
parser/ — парсинг CSV
provider/ — провайдеры данных
renderer/ — запись в БД
request/ — SQL-запросы
AppWithSpringJava — точка входа

resources/

application.properties
products.csv
category.csv
schema.sql
logback.xml


---

## База данных

Используется встраиваемая база **H2 (in-memory)**.

Создание БД выполняется при старте приложения через:

```java
new EmbeddedDatabaseBuilder()
    .setType(EmbeddedDatabaseType.H2)
    .addScript("schema.sql")
    .build();
SQL-скрипт
CREATE TABLE CATEGORIES (
    category_id INT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE PRODUCTS (
    product_id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    category_id INT,
    price DECIMAL(10,2),
    stock_quantity INT,
    image_url VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
);
Загрузка данных

Данные читаются из CSV-файлов:

src/main/resources/products.csv
src/main/resources/category.csv

Через:

ResourceFileReader

CSVParser

ConcreteProductProvider

ConcreteCategoryProvider

Запись в базу данных

Реализован класс:

DataBaseRenderer

Он:

читает категории

читает продукты

вставляет их в таблицы H2 через JdbcTemplate

SQL-запрос

Класс:

CategoryRequest

Выполняет запрос:

SELECT c.name, COUNT(p.product_id) AS cnt
FROM CATEGORIES c
JOIN PRODUCTS p ON c.category_id = p.category_id
GROUP BY c.name
HAVING COUNT(p.product_id) > 1;

Вывод через logback (INFO):

Категория: Toys | товаров: 2
Логирование

Используется logback:

src/main/resources/logback.xml

Уровень:

INFO
Запуск приложения
gradle run

При запуске:

создаётся БД H2

создаются таблицы

загружаются CSV

данные записываются в БД

выполняется SQL-запрос

результат выводится в консоль

UML-диаграмма классов (mermaid)

classDiagram

class AppWithSpringJava {
    +main()
}

class Renderer {
    <<interface>>
    +render()
}

class DataBaseRenderer {
    -ProductProvider productProvider
    -ConcreteCategoryProvider categoryProvider
    -JdbcTemplate jdbc
    +render()
}

class ProductProvider {
    <<interface>>
    +getProducts()
}

class ConcreteProductProvider {
    -Reader reader
    -Parser parser
    +getProducts()
}

class ConcreteCategoryProvider {
    -ResourceFileReader reader
    +getCategories()
}

class Parser {
    <<interface>>
    +parse()
}

class CSVParser {
    +parse()
}

class Reader {
    <<interface>>
    +read()
}

class ResourceFileReader {
    +read()
    +readFile()
}

class CategoryRequest {
    -JdbcTemplate jdbc
    +printCategoriesWithManyProducts()
}

class Product {
    +productId
    +name
    +description
    +categoryId
    +price
    +stockQuantity
    +imageUrl
    +createdAt
    +updatedAt
}

class Category {
    +categoryId
    +name
    +description
}

AppWithSpringJava --> Renderer
Renderer <|.. DataBaseRenderer

DataBaseRenderer --> ProductProvider
DataBaseRenderer --> ConcreteCategoryProvider
DataBaseRenderer --> JdbcTemplate

ProductProvider <|.. ConcreteProductProvider
Parser <|.. CSVParser
Reader <|.. ResourceFileReader

ConcreteProductProvider --> Parser
ConcreteProductProvider --> Reader
ConcreteCategoryProvider --> ResourceFileReader

CategoryRequest --> JdbcTemplate

Результат

В результате лабораторной работы:

подключена H2

создана структура БД

реализована загрузка CSV

реализована запись в БД

реализован SQL-запрос

настроено логирование

приложение успешно запускается через gradle run

Вывод

В ходе лабораторной работы освоены:

подключение встроенной БД H2

работа с JDBC через Spring

выполнение SQL-скриптов при старте

загрузка данных из CSV

проектирование слоёв Provider / Renderer / Request

логирование через logback