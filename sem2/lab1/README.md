# Лабораторная работа №1. Spring Boot

Цель работы — создать Spring Boot REST-приложение для заметок. Заметки хранятся в памяти и нумеруются с нуля. Выполнены варианты 2 и 6 дополнительного задания.

Из каталога `sem2` запустите `./gradlew :lab1:bootRun` на Java 17.

| Запрос | Результат |
| --- | --- |
| `GET /` | Приветствие |
| `GET /messages` | Список заметок |
| `POST /messages` | Добавление текста из тела запроса |
| `PUT /messages/{index}` | Замена текста заметки |
| `DELETE /messages/{index}` | Удаление заметки |
| `GET /messages/count` | Количество заметок |

```sh
curl -H 'Content-Type: text/plain; charset=UTF-8' --data-binary 'Первая заметка' http://localhost:8080/messages
curl http://localhost:8080/messages/count
curl -X PUT -H 'Content-Type: text/plain; charset=UTF-8' --data-binary 'Новый текст' http://localhost:8080/messages/0
curl -X DELETE http://localhost:8080/messages/0
```

Если заметка пустая или состоит из пробелов, сервер отвечает HTTP 400. Для отсутствующего индекса сервер возвращает HTTP 404.
