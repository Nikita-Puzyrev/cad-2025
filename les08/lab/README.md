# Лабораторная работа №4  

**Тема:** Технологии работы с базами данных. JPA. Spring Data

---

## Цель работы

Освоить работу с ORM-фреймворком Hibernate, реализовать слой репозиториев и сервисов,
настроить подключение к базе данных H2 через HikariCP и реализовать транзакционное
создание заказа на основании данных, загруженных из CSV-файлов.

## Цель работы

Освоить работу с ORM-фреймворком Hibernate, реализовать слой репозиториев и сервисов,
настроить подключение к базе данных H2 через HikariCP и реализовать транзакционное
создание заказа на основании данных, загруженных из CSV-файлов.

ru.bsuedu.cad.lab
├── config
│ ├── AppConfig
│ └── DatabaseConfig
├── entity
│ ├── Category
│ ├── Product
│ ├── Customer
│ ├── Order
│ └── OrderDetail
├── repository
│ ├── CategoryRepository
│ ├── ProductRepository
│ ├── CustomerRepository
│ ├── OrderRepository
│ └── OrderDetailRepository
├── service
│ ├── DataLoaderService
│ └── OrderService
└── app
└── App

JPA-сущности

Реализованы сущности:

Category — категории товаров

Product — товары

Customer — клиенты

Order — заказы

OrderDetail — позиции заказа

Связи:

Category 1-N Product

Customer 1-N Order

Order 1-N OrderDetail

Product 1-N OrderDetail

Результат выполнения

При запуске:

gradle run

В консоли выводится:

Создан заказ: id=1, статус=NEW
Получено заказов из БД: 1
Заказов в базе: 1
Клиент: Алексей Иванов
Товар: Сухой корм для собак

## UML-диаграмма классов

```mermaid
classDiagram

class Category
class Product
class Customer
class Order
class OrderDetail

Category "1" --> "many" Product
Customer "1" --> "many" Order
Order "1" --> "many" OrderDetail
Product "1" --> "many" OrderDetail

class CategoryRepository
class ProductRepository
class CustomerRepository
class OrderRepository
class OrderDetailRepository

class OrderService
class DataLoaderService
class App

CategoryRepository --> Category
ProductRepository --> Product
CustomerRepository --> Customer
OrderRepository --> Order
OrderDetailRepository --> OrderDetail

OrderService --> OrderRepository
OrderService --> CustomerRepository
OrderService --> ProductRepository

DataLoaderService --> CategoryRepository
DataLoaderService --> CustomerRepository
DataLoaderService --> ProductRepository

App --> DataLoaderService
App --> OrderService