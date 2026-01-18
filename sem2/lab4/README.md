# Отчет о выполнении лабораторной работы №4

# Аллис Александр Сергеевич, гр. 12002309
(https://github.com/ALEXANDER-ALLIS/cad-2025)

## Цель работы

Разработать серверное приложение на Spring Boot, реализовав REST API для работы с основными сущностями.

## Выполнение работы

Создана базовая структура проекта Spring Boot:

1. Использован шаблон проекта с Maven/Gradle

2. Добавлены зависимости: Spring Web, Spring Data JPA, H2 Database (для разработки)

3. Настроена конфигурация приложения

Реализованы модели данных (Entity):

1. Member с полями: id, firstName, lastName, email, phoneNumber, registrationDate

2. Subscription с полями: id, type, price, startDate, endDate, isActive

3. Trainer с полями: id, name, specialization, experienceYears

4. Workout с полями: id, name, description, duration, trainer, scheduleTime

5. Установлены отношения между сущностями (OneToOne, ManyToOne)

Созданы репозитории (Repository):

1. MemberRepository extends JpaRepository

2. SubscriptionRepository extends JpaRepository

3. TrainerRepository extends JpaRepository

4. WorkoutRepository extends JpaRepository

Разработаны REST контроллеры:

1. MemberController с endpoints: GET /api/members, GET /api/members/{id}, POST /api/members

2. SubscriptionController

3. TrainerController

4. WorkoutController

Реализована бизнес-логика в сервисном слое:

1. MemberService с методами для CRUD операций

2. Валидация данных при создании/обновлении

3. Обработка исключений

Настроена база данных:

1. Использована H2 Database для разработки

2. Созданы начальные данные через data.sql

3. Настроены миграции (при необходимости)

Добавлена документация:

1. Swagger/OpenAPI для тестирования API

2. CORS конфигурация для работы с клиентами

## Выводы

Разработано полнофункциональное серверное приложение на Spring Boot

Реализовано REST API для всех основных операций фитнес-клуба

Приложение использует слоистую архитектуру (Controller-Service-Repository)

Обеспечена возможность легкого масштабирования и добавления новых функций

API готово к интеграции с различными клиентами (веб, мобильные приложения)