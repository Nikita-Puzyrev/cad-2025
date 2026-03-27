\# Лабораторная работа №2

\*\*Конфигурирование приложение Spring c помощью аннотаций. Применение AOП для логирования\*\*



\## Цель работы 



Целью лабораторной работы является изучение:

\- конфигурирования Spring-приложения с помощью аннотаций;

\- внедрения зависимостей;

\- использования конфигурационного файла;

\- жизненного цикла бинов;

\- аспектно-ориентированного программирования (АОП);

\- расширяемости архитектуры за счёт интерфейсов.



---



\## Описание приложения



Приложение представляет собой консольное Spring-приложение, которое:

\- загружает данные о товарах из CSV-файла;

\- парсит CSV в список объектов `Product`;

\- измеряет время парсинга с помощью AOP;

\- выводит результат в HTML-файл в виде таблицы;

\- использует события жизненного цикла бина для логирования момента инициализации.



---



\## Структура проекта



les04/lab

└── app

├── src/main/java/ru/bsuedu/cad/lab

│ ├── aop

│ │ └── CSVParserTimingAspect.java

│ ├── model

│ │ └── Product.java

│ ├── parser

│ │ ├── Parser.java

│ │ └── CSVParser.java

│ ├── provider

│ │ ├── ProductProvider.java

│ │ └── ConcreteProductProvider.java

│ ├── reader

│ │ ├── Reader.java

│ │ └── ResourceFileReader.java

│ ├── renderer

│ │ ├── Renderer.java

│ │ ├── ConsoleTableRenderer.java

│ │ └── HTMLTableRenderer.java

│ └── AppWithSpringJava.java

└── src/main/resources

├── application.properties

└── products.csv





---



## UML-диаграмма классов

```mermaid
classDiagram

class Product {
  long productId
  String name
  String description
  int categoryId
  BigDecimal price
  int stockQuantity
  String imageUrl
  Date createdAt
  Date updatedAt
}

class Reader {
  <<interface>>
  read() String
}

class ResourceFileReader {
  read() String
}

class Parser {
  <<interface>>
  parse(String) List~Product~
}

class CSVParser {
  parse(String) List~Product~
}

class ProductProvider {
  <<interface>>
  getProducts() List~Product~
}

class ConcreteProductProvider {
  Reader reader
  Parser parser
  getProducts() List~Product~
}

class Renderer {
  <<interface>>
  render()
}

class ConsoleTableRenderer {
  render()
}

class HTMLTableRenderer {
  render()
}

class CSVParserTimingAspect

Reader <|.. ResourceFileReader
Parser <|.. CSVParser
ProductProvider <|.. ConcreteProductProvider
Renderer <|.. ConsoleTableRenderer
Renderer <|.. HTMLTableRenderer

ConcreteProductProvider --> Reader
ConcreteProductProvider --> Parser
ConcreteProductProvider --> Product

ConsoleTableRenderer --> ProductProvider
HTMLTableRenderer --> ProductProvider

CSVParserTimingAspect ..> CSVParser
```


\## Вывод



В ходе выполнения лабораторной работы были изучены и применены:

\- внедрение зависимостей с использованием аннотаций Spring;

\- конфигурирование приложения через файл application.properties;

\- события жизненного цикла бинов для отслеживания момента инициализации;

\- аспектно-ориентированное программирование (AOP) для замера времени выполнения парсинга;

\- расширяемая архитектура с использованием интерфейсов и нескольких реализаций.



Все требования лабораторной работы выполнены, приложение корректно запускается и демонстрирует ожидаемое поведение.





