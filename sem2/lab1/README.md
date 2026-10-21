# Отчет о лаботаротоной работе 
# Галуцкой Анастасии Александровны гр. 12002353
(https://github.com/Scissorluv/cad-2025)

## Цель работы

Разработать серверное приложение на Spring Boot для управления фитнес-клубом, реализовав REST API для работы с основными сущностями: участниками, абонементами, тренерами и тренировками.

## Выполнение работы

Создана базовая структура проекта Spring Boot:

Использован шаблон проекта с Maven/Gradle

Добавлены зависимости: Spring Web, Spring Data JPA, H2 Database (для разработки)

Настроена конфигурация приложения

Реализованы модели данных (Entity):

Member (Участник) с полями: id, firstName, lastName, email, phoneNumber, registrationDate

Subscription (Абонемент) с полями: id, type, price, startDate, endDate, isActive

Trainer (Тренер) с полями: id, name, specialization, experienceYears

Workout (Тренировка) с полями: id, name, description, duration, trainer, scheduleTime

Установлены отношения между сущностями (OneToOne, ManyToOne)

Созданы репозитории (Repository):

MemberRepository extends JpaRepository

SubscriptionRepository extends JpaRepository

TrainerRepository extends JpaRepository

WorkoutRepository extends JpaRepository

Разработаны REST контроллеры:

MemberController с endpoints: GET /api/members, GET /api/members/{id}, POST /api/members

SubscriptionController для управления абонементами

TrainerController для работы с тренерами

WorkoutController для управления тренировками

Реализована бизнес-логика в сервисном слое:

MemberService с методами для CRUD операций

Валидация данных при создании/обновлении

Обработка исключений

Настроена база данных:

Использована H2 Database для разработки

Созданы начальные данные через data.sql

Настроены миграции (при необходимости)

Добавлена документация:

Swagger/OpenAPI для тестирования API

CORS конфигурация для работы с клиентами

## Выводы

Разработано полнофункциональное серверное приложение на Spring Boot

Реализовано REST API для всех основных операций фитнес-клуба

Приложение использует слоистую архитектуру (Controller-Service-Repository)

Обеспечена возможность легкого масштабирования и добавления новых функций

API готово к интеграции с различными клиентами (веб, мобильные приложения)

