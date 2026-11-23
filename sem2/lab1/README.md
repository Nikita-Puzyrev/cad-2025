# Лабораторная работа №1. Введение в Spring Boot

**Цель работы:** получить практические навыки работы с Spring Framework.

## 1. Теоретические сведения

Spring — фреймворк для разработки приложений на языке Java, предоставляющий
модульную архитектуру и набор инструментов для создания веб-приложений.
Основные возможности:

- **внедрение зависимостей (Dependency Injection)** — управление зависимостями
  между компонентами, упрощающее тестирование и повторное использование кода;
- **аспектно-ориентированное программирование (AOP)** — вынесение сквозной
  функциональности (логирование, транзакции, безопасность) в отдельные модули;
- **модульность** — подключаются только нужные модули: веб, доступ к данным,
  безопасность;
- **Spring MVC** — построение веб-приложений с разделением модели,
  представления и контроллера;
- **управление транзакциями** — декларативное описание границ транзакций.

Spring Boot — надстройка над Spring, которая снимает ручную настройку:
автоматическая конфигурация, встроенный сервер Tomcat, стартеры зависимостей.

## 2. Создание проекта

Проект создан на сайте <https://start.spring.io/> со следующими параметрами:

| Параметр | Значение |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 3.3.5 |
| Group | ru.belgu.cad |
| Artifact | lab1 |
| Packaging | Jar |
| Java | 17 |
| Зависимости | Spring Web |

Структура проекта:

```
lab1
├── pom.xml
└── src
    ├── main
    │   ├── java/ru/belgu/cad/lab1
    │   │   ├── Lab1Application.java
    │   │   ├── Message.java
    │   │   └── MessageController.java
    │   └── resources/application.properties
    └── test/java/ru/belgu/cad/lab1/MessageControllerTest.java
```

Класс `Lab1Application` оставлен без изменений относительно сгенерированного:
аннотация `@SpringBootApplication` объединяет `@Configuration`,
`@EnableAutoConfiguration` и `@ComponentScan`, а метод `main` запускает
приложение через `SpringApplication.run`.

В `pom.xml` подключён стартер веб-приложений:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 3. Реализация приложения

Приложение хранит заметки в памяти и предоставляет REST-интерфейс для работы
с ними. Класс `MessageController` помечен аннотацией `@RestController`, поэтому
возвращаемые значения сериализуются в JSON.

Каждое сообщение хранится вместе с датой создания — это требуется для выборки
сообщений, опубликованных после указанного момента:

```java
public record Message(long id, String text, LocalDateTime createdAt) {
}
```

Реализованные обработчики:

| Метод и адрес | Назначение |
|---|---|
| `GET /` | приветствие «Hello, World!» |
| `GET /messages` | список сообщений; параметр `after` — фильтр по дате создания |
| `POST /messages` | публикация сообщения |
| `GET /messages/{index}` | сообщение по индексу |
| `PUT /messages/{index}` | замена сообщения по индексу |
| `DELETE /messages/{index}` | удаление сообщения по индексу |
| `DELETE /messages` | очистка списка |
| `GET /messages/count` | количество сообщений |

Фрагмент контроллера — публикация с проверкой на пустое сообщение и
ограничением количества:

```java
@PostMapping("/messages")
public ResponseEntity<?> publishMessage(@RequestBody String message) {
    String text = message == null ? "" : message.trim();
    if (text.isEmpty()) {
        return ResponseEntity.badRequest().body("Сообщение не может быть пустым");
    }
    synchronized (userMessages) {
        if (userMessages.size() >= LIMIT) {
            userMessages.remove(0);
        }
        userMessages.add(new Message(sequence.getAndIncrement(), text, LocalDateTime.now()));
    }
    return ResponseEntity.status(HttpStatus.CREATED)
            .body("Message published successfully!");
}
```

## 4. Дополнительное задание

В перечне дополнительных заданий шесть вариантов. По правилу выбора варианта
((26 − 1) mod 6) + 1 основным является **вариант 2** — проверка на пустое
сообщение при публикации. Дополнительно реализованы остальные пункты перечня,
поскольку они дополняют друг друга и не конфликтуют:

| Вариант | Реализация |
|---|---|
| 1. `clearAllMessages()` | `DELETE /messages` |
| 2. Проверка пустого сообщения | `POST` и `PUT` возвращают 400 с текстом ошибки |
| 3. `getMessageByIndex(int index)` | `GET /messages/{index}` |
| 4. Ограничение количества сообщений | предел `LIMIT = 20`, при переполнении удаляется самое старое |
| 5. Выборка после даты | `GET /messages?after=2026-11-20T18:00:00` |
| 6. `countMessages()` | `GET /messages/count` |

## 5. Результаты работы

Сборка и тесты:

```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Приложение запущено командой `java -jar target/lab1-1.0.0.jar`. Запросы
выполнены к `http://localhost:8080`; результаты приведены в таблице.

| № | Запрос | Код | Ответ |
|---|---|---|---|
| 1 | `GET /` | 200 | `Hello, World!` |
| 2 | `POST /messages`, тело «первая заметка» | 201 | `Message published successfully!` |
| 3 | `POST /messages`, тело из пробелов | 400 | `Сообщение не может быть пустым` |
| 4 | `GET /messages` | 200 | `[{"id":2,"text":"первая заметка","createdAt":"2026-11-20T18:20:52.5291859"}]` |
| 5 | `GET /messages/0` | 200 | `{"id":2,"text":"первая заметка",…}` |
| 6 | `PUT /messages/0`, тело «изменённая заметка» | 200 | `Message updated successfully!` |
| 7 | `GET /messages/0` после изменения | 200 | `{"id":2,"text":"изменённая заметка",…}` |
| 8 | `GET /messages/count` | 200 | `1` |
| 9 | `DELETE /messages/0` | 200 | `Message deleted successfully!` |
| 10 | `GET /messages/0` после удаления | 404 | `Message not found at index 0` |
| 11 | `DELETE /messages` | 200 | `All messages cleared` |
| 12 | `GET /messages/count` после очистки | 200 | `0` |

## 6. Выводы

В ходе работы освоен процесс создания проекта Spring Boot через Spring
Initializr, изучена структура Maven-проекта и назначение файла `pom.xml`.
Реализовано веб-приложение для хранения заметок: контроллер обрабатывает
запросы GET, POST, PUT и DELETE, выполняет операции CRUD над списком сообщений
и возвращает корректные коды состояния HTTP. Выполнены все пункты
дополнительного задания: проверка пустого сообщения, очистка списка, получение
сообщения по индексу, ограничение количества хранимых сообщений, выборка по
дате публикации и подсчёт количества. Написаны пять автоматизированных тестов
(MockMvc), все проходят. Приложение проверено реальными запросами.

## 7. Запуск

```bash
mvn package
java -jar target/lab1-1.0.0.jar
```
