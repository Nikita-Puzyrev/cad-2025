# Лабораторная работа 4. Технологии работы с базами данных. JPA. Spring Data

Пришло время выполнить рефакторинг нашего проекта. Выбросить все ненужно, дополнить и развить нужный функционал. Магазин зоотоваров 🐶 сам себя не разработает 😂.

В этой работе мы перейдем с использование Spring JDBC на использование ORM Hibernate и Spring Data. Расширим наше приложение новыми сущностями и приведем структуру приложения в соответствие со "слоистой архитектурой".

1. Созданий новое приложение или скопируйте результат выполнения лабораторной работы №3 в директорию [/les08/lab/](/les08/lab/). Изменений будет много, возможно для вас будет проще создать проект заново.
2. Создайте DataSource соответствующий следующим требованиям
   1. Должна использоваться база данных H2
   2. Для реализации DataSource необходимо использовать библиотеку HikariCP, а именно HikariDataSource
   3. Для работы с базой данных должна использоваться библиотека HIbernate, использующая технологию ORM 
   4. Схема схема данных должна создаваться автоматически на основании JPA сущностей.
   
3. Структура пакетов проекта должна иметь следующий вид
    - ru.bsuedu.cad.lab - основной пакет
      - ru.bsuedu.cad.lab.entity -  JPA сущности
      - ru.bsuedu.cad.lab.repository - репозитории
      - ru.bsuedu.cad.lab.service - сервисы
      - ru.bsuedu.cad.lab.app - приложение
4. В пакете ru.bsuedu.cad.lab.entity создайте JPA сущности для следующей схемы базы данных.
   
   ``` mermaid
    erDiagram
    CATEGORIES {
        int category_id PK
        string name
        string description
    }
    
    PRODUCTS {
        int product_id PK
        string name
        string description
        int category_id FK
        decimal price
        int stock_quantity
        string image_url
        datetime created_at
        datetime updated_at
    }

    CUSTOMERS {
        int customer_id PK
        string name
        string email
        string phone
        string address
    }

    ORDERS {
        int order_id PK
        int customer_id FK
        datetime order_date
        decimal total_price
        string status
        string shipping_address
    }

    ORDER_DETAILS {
        int order_detail_id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price
    }

    CATEGORIES ||--o{ PRODUCTS : "содержит"
    CUSTOMERS ||--o{ ORDERS : "размещает"
    ORDERS ||--o{ ORDER_DETAILS : "содержит"
    PRODUCTS ||--o{ ORDER_DETAILS : "включен в"
   ```

5. В пакете ru.bsuedu.cad.lab.repository реализуйте репозитории для каждой сущности. Репозитории содержать методы по созданию, получение записи по идентификатору и получения всех записей для каждой сущности.
6. В пакете ru.bsuedu.cad.lab.service создайте сервисы для создания заказа и получению списка всех заказов.
7. В пакете ru.bsuedu.cad.lab.app реализуйте клиент для сервиса создания заказа, который создает новый заказ. Создание заказа должно выполняться в рамках транзакции. Выведите информацию о создании заказа в лог. Докажите, что заказ сохранился в базе данных. (Для того, чтобы создать заказ, необходимо заполнить таблицы базы данных на основании CSV файлов ([category.csv](./assets/category.csv), [customer.csv](lab/app/src/main/resources/customer.csv), [product.csv](./assets/product.csv)). Сделайте это любым удобным для вас способом. )
8. Приложение должно запускаться с помощью команды ```gradle run```, выводить необходимую информацию в консоль и успешно завершаться.
9.  Оформите отчет о выполнении лабораторной работы в виде файла  README.md в директории [les08/lab](/les08/lab/). Отчет должен содержать обновленную  UML-диаграмму классов в формате [mermaid](https://mermaid.js.org/).

## Вопросы для защиты

JPA

1. Что такое JPA и для чего оно используется?
2. Чем JPA отличается от Hibernate?
3. Что делает аннотация @Entity?
4. Для чего нужна аннотация @Table?
5. Как обозначить первичный ключ в JPA?
6. Что делает аннотация @GeneratedValue?
7. Какие бывают стратегии генерации идентификаторов в JPA?
8. Чем отличается @Column(name = "field_name") от использования имени поля напрямую?
9. Как задать связь “один ко многим” (@OneToMany) в JPA?
10. Как задать связь “многие ко многим” (@ManyToMany) в JPA?

Spring Data

1. Что такое Spring Data и зачем оно нужно?
2. Что делает интерфейс CrudRepository?
3. Чем JpaRepository отличается от CrudRepository?
4. Как создать свой репозиторий в Spring Data JPA?
5. Как выполнить поиск по ID с помощью Spring Data JPA?
6. Как добавить новую запись в базу данных через Spring Data JPA?
7. Как удалить объект из базы данных в Spring Data JPA?
8. Как написать свой SQL-запрос в Spring Data JPA?
9. Что такое @Transactional и зачем она нужна?
10. Какие аннотации нужны для работы с JPA-сущностями?