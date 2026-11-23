# Лабораторная работа №4. Взаимодействие Spring и PostgreSQL

**Цель работы:** получить практические навыки работы с Spring Framework
(взаимодействие с СУБД PostgreSQL посредством Spring Data JPA).

## 1. Теоретические сведения

Spring Data JPA — модуль Spring, упрощающий работу с реляционными базами
данных через JPA (Jakarta Persistence API). Репозиторий описывается
интерфейсом-наследником `JpaRepository`; реализацию генерирует Spring, а запросы
можно задавать именем метода или аннотацией `@Query`. Сущности связываются
отношениями «один ко многим» (`@OneToMany`/`@ManyToOne`), схема таблиц
формируется Hibernate по описанию сущностей (`spring.jpa.hibernate.ddl-auto`).

## 2. Настройка подключения

Зависимости: `spring-boot-starter-data-jpa`, драйвер `org.postgresql:postgresql`,
для тестов — встроенная база H2.

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

Тесты выполняются на встроенной H2 в режиме совместимости с PostgreSQL
(`MODE=PostgreSQL`), поэтому сборка не требует запущенного сервера.

## 3. Структура приложения

Приложение развивает список задач из лабораторных работ №2 и №3: данные
хранятся не в памяти, а в PostgreSQL.

```
ru.belgu.cad.lab4
├── domain      Task, SubTask, AppUser, LoginHistory, TaskHistory, Priority
├── repository  TaskRepository, SubTaskRepository, AppUserRepository,
│               TaskHistoryRepository, LoginHistoryRepository
├── service     TaskService
├── security    SecurityConfig, DbUserDetailsService, LoginSuccessListener
├── config      DataInitializer
├── web         TaskController
└── Lab4Application
```

### Модель данных

| Таблица | Поля | Назначение |
|---|---|---|
| `tasks` | id, title, description, completed, **created_at**, **priority**, **category**, author_id | задачи (таблица модернизирована) |
| `sub_tasks` | id, description, completed, task_id | подзадачи, связь «один ко многим» |
| `app_users` | id, name, email, username, password, role | пользователи, связь с задачами |
| `login_history` | id, login_time, user_id, ip_address | история входов |
| `task_history` | id, changed_at, old_state, new_state, task_id | история изменений задач |

Связи: `tasks.author_id → app_users.id` (многие к одному),
`sub_tasks.task_id → tasks.id`, `task_history.task_id → tasks.id`,
`login_history.user_id → app_users.id`. Схема создаётся Hibernate при первом
запуске.

### Бизнес-логика

`TaskService` реализует операции над задачами и ведение истории:

```java
@Transactional
public void changeStatus(Long id, boolean completed) {
    Task task = tasks.findById(id).orElseThrow();
    String oldState = task.state();
    task.setCompleted(completed);
    Task saved = tasks.save(task);
    taskHistory.save(new TaskHistory(saved, oldState, saved.state()));
}
```

История входов заполняется слушателем события аутентификации:

```java
@Component
public class LoginSuccessListener
        implements ApplicationListener<AuthenticationSuccessEvent> {
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String ip = RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes a
                ? a.getRequest().getRemoteAddr() : "unknown";
        taskService.logLogin(event.getAuthentication().getName(), ip);
    }
}
```

Пользователи хранятся в таблице `app_users`; `DbUserDetailsService` читает их
при входе, пароли — BCrypt-хеши. Роли и права сохранены из лабораторной
работы №3: GUEST — просмотр, USER — добавление задач и подзадач,
MODERATOR — изменение статуса, удаление и страница истории, ADMIN — то же плюс
полный доступ к справочнику.

## 4. Выполнение дополнительного задания

| Требование | Реализация |
|---|---|
| Модернизировать `tasks`: время создания, приоритет, категория | поля `created_at` (LocalDateTime), `priority` (перечисление LOW/NORMAL/HIGH), `category`; отображаются в списке и используются в фильтре |
| Таблица `SubTask` (ID, описание, статус, ID родительской задачи), «один ко многим» | сущность `SubTask`, `@OneToMany(mappedBy = "task", cascade = ALL, orphanRemoval = true)`; в интерфейсе — список подзадач, добавление и переключение статуса |
| Таблица `User` (ID, имя, email, пароль) и связь с `Task` | сущность `AppUser`, `@ManyToOne` `Task.author`; в списке задач отображается автор |
| Таблица `LoginHistory` (ID, дата входа, ID пользователя, IP) | сущность `LoginHistory`, заполняется при успешной аутентификации; выводится на странице `/history` |
| Таблица `TaskHistory` (ID, дата изменения, старое и новое состояние, ID задачи) | сущность `TaskHistory`; записывается при создании, смене статуса, добавлении подзадачи и удалении задачи; выводится на `/history` |

## 5. Результаты работы

Сборка и автотесты (H2 в режиме PostgreSQL):

```
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Запуск на реальном сервере **PostgreSQL 17.2** (база `todo_db`, локальный порт
5432). Созданные таблицы:

```
 public | app_users     | таблица | postgres
 public | login_history | таблица | postgres
 public | sub_tasks     | таблица | postgres
 public | task_history  | таблица | postgres
 public | tasks         | таблица | postgres
```

Содержимое таблиц после прогона:

| Таблица | Записей |
|---|---|
| `tasks` | 3 |
| `sub_tasks` | 3 |
| `app_users` | 4 |
| `task_history` | 3 |
| `login_history` | 2 |

Проверка работы приложения через HTTP (сеанс для каждой роли, вход через форму
с CSRF-токеном):

| № | Действие | Результат |
|---|---|---|
| 1 | Вход пользователем `user`, `GET /` | 200, отображаются задачи из базы и автор |
| 2 | `POST /addTask` («Проверка PostgreSQL», HIGH, «Учеба») | перенаправление на `/`, задача появилась в списке и в таблице `tasks` |
| 3 | `POST /addSubTask/{id}` | подзадача сохранена в `sub_tasks` и отображена под задачей |
| 4 | USER: `GET /deleteTask/{id}` | **403 Forbidden** (права только у модератора и администратора) |
| 5 | Вход `moderator`, `GET /history` | 200, в истории изменений есть запись о созданной задаче, в истории входов — `user` и `moderator` |
| 6 | MODERATOR: `GET /deleteTask/{id}` | 200, задача удалена вместе с подзадачами и записями истории |
| 7 | Перезапуск приложения, `GET /` | данные сохранились: 3 задачи, 1 выполнена — чтение идёт из PostgreSQL |

Фильтрация по категории, приоритету и статусу выполняется на уровне запроса к
базе (`@Query` с `left join fetch` подзадач и фильтры в `TaskService`).

## 6. Выводы

Освоено взаимодействие Spring-приложения с СУБД PostgreSQL: настройка
источника данных, отображение объектной модели на таблицы средствами
Hibernate, работа с репозиториями Spring Data JPA, связи «один ко многим» и
«многие к одному», транзакционное ведение истории изменений. Схема данных
расширена до пяти таблиц: задачи с временем создания, приоритетом и
категорией, подзадачи, пользователи, история входов и история изменений
задач. Проверено, что данные сохраняются между запусками приложения, а права
доступа контролируются на сервере.

## 7. Запуск

```bash
# база данных
createdb -h localhost -U postgres todo_db

mvn package
java -jar target/lab4-1.0.0.jar
```

Приложение: <http://localhost:8080/>. Логины — `admin`, `moderator`, `user`,
`guest`, пароль `password`. Страница истории — `/history` (MODERATOR, ADMIN).
