# Лабораторная работа №6

## Разработка Web-приложений с использованием технологии Spring MVC

---

## REST API

Базовый URL:

http://localhost:8080/product-table/api/orders

### Методы:

- GET /api/orders — получить список заказов  
- GET /api/orders/{id} — получить заказ по ID  
- POST /api/orders — создать заказ  
- PUT /api/orders/{id} — обновить заказ  
- DELETE /api/orders/{id} — удалить заказ  

Пример создания заказа:

```json
{
  "status": "NEW",
  "shippingAddress": "Moscow, Lenina 1"
}
```

## Тестирование

Создана коллекция Postman **Lab6 Orders API**, содержащая все 5 REST-запросов.  
API успешно протестирован (HTTP 200 / 201 / 204).

---

## Web-интерфейс

Подключён **Thymeleaf**.  
Реализованы страницы:

- список заказов  
- создание заказа  
- редактирование  
- удаление  

Доступ: http://localhost:8080/product-table


---

## Сборка и деплой

Сборка проекта: gradle clean war

WAR-файл: build/libs/product-table.war


Деплой:

Скопировать WAR-файл в папку C:\tomcat11\webapps

и перезапустить **Apache Tomcat 11**.

---

## UML-диаграмма (Mermaid)

```mermaid
classDiagram

class Order {
    Integer id
    LocalDateTime orderDate
    BigDecimal totalPrice
    String status
    String shippingAddress
    List<OrderDetail> details
}

class Customer {
    Integer id
    String name
}

class Product {
    Integer id
    String name
    BigDecimal price
}

class OrderDetail {
    Integer id
    Integer quantity
    BigDecimal price
}

Order "1" --> "*" OrderDetail
Order "*" --> "1" Customer
OrderDetail "*" --> "1" Product
