# Отчет о лабораторной работе №4

# Технологии работы с базами данных. JPA. Spring Data

## Цель работы

Целью лабораторной работы является выполнение рефакторинга приложения магазина зоотоваров, переход от использования Spring JDBC к использованию JPA, Hibernate и Spring Data, а также приведение структуры проекта к слоистой архитектуре.

В ходе работы необходимо настроить подключение к базе данных H2 через HikariCP, реализовать JPA-сущности, создать репозитории для работы с базой данных, реализовать сервис создания заказов и проверить сохранение заказа в базе данных.

## Выполнение работы

В качестве основы был использован результат предыдущей лабораторной работы. Проект был перенесен в директорию `/les08/lab/`.

В новой версии приложения была изменена архитектура проекта. Вместо прямой работы с базой данных через `JdbcTemplate` используется ORM Hibernate и Spring Data JPA.

Структура пакетов была приведена к следующему виду:

```text id="szs8v2"
ru.bsuedu.cad.lab
├── entity
├── repository
├── service
└── app
```

Назначение пакетов:

* `ru.bsuedu.cad.lab.entity` — JPA-сущности;
* `ru.bsuedu.cad.lab.repository` — репозитории Spring Data JPA;
* `ru.bsuedu.cad.lab.service` — сервисный слой приложения;
* `ru.bsuedu.cad.lab.app` — запуск приложения и клиентский код.

### Подключение зависимостей

Для работы с JPA, Hibernate, Spring Data, H2 и HikariCP были добавлены необходимые зависимости.

Фрагмент файла `build.gradle.kts`:

```kotlin id="i3lm0f"
dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")
    implementation("org.springframework:spring-tx:6.2.2")

    implementation("org.springframework.data:spring-data-jpa:3.4.2")

    implementation("org.hibernate.orm:hibernate-core:6.6.5.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.3.232")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

Главный класс приложения был изменен на:

```kotlin id="k6cwgg"
application {
    mainClass = "ru.bsuedu.cad.lab.app.App"
}
```

### Конфигурация приложения

Для настройки базы данных, Hibernate, EntityManagerFactory, менеджера транзакций и репозиториев был создан класс `Config`.

```java id="vo7uq7"
package ru.bsuedu.cad.lab;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.lab.repository")
@EnableTransactionManagement
public class Config {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl("jdbc:h2:mem:petshop;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setDriverClassName("org.h2.Driver");

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource);
        factory.setPackagesToScan("ru.bsuedu.cad.lab.entity");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setJpaProperties(hibernateProperties());

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "true");

        return properties;
    }
}
```

В данном классе:

* `HikariDataSource` используется для подключения к базе данных H2;
* `LocalContainerEntityManagerFactoryBean` настраивает работу JPA и Hibernate;
* `JpaTransactionManager` отвечает за управление транзакциями;
* `@EnableJpaRepositories` включает поиск Spring Data JPA репозиториев;
* `@EnableTransactionManagement` включает поддержку транзакций.

Схема базы данных создается автоматически на основании JPA-сущностей благодаря параметру:

```java id="hyiasf"
hibernate.hbm2ddl.auto=create-drop
```

### JPA-сущности

В пакете `ru.bsuedu.cad.lab.entity` были созданы сущности, соответствующие схеме базы данных:

* `Category`;
* `Product`;
* `Customer`;
* `Order`;
* `OrderDetail`.

### Сущность Category

```java id="3b1c6p"
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(Integer categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }
}
```

Сущность `Category` описывает категорию товаров.
Связь с товарами реализована через `@OneToMany`.

### Сущность Product

```java id="xr8j7p"
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Product() {
    }
}
```

Сущность `Product` описывает товар.
Каждый товар относится к одной категории, поэтому используется связь `@ManyToOne`.

### Сущность Customer

```java id="m30tm4"
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone;

    @Column(length = 1000)
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(Integer customerId, String name, String email, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
```

Сущность `Customer` описывает покупателя.
Один покупатель может иметь несколько заказов.

### Сущность Order

```java id="o9jfp4"
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String status;

    @Column(name = "shipping_address", length = 1000)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> details = new ArrayList<>();

    public Order() {
    }

    public void addDetail(OrderDetail detail) {
        details.add(detail);
        detail.setOrder(this);
    }
}
```

Сущность `Order` описывает заказ.
Заказ связан с покупателем через `@ManyToOne`, а список позиций заказа связан через `@OneToMany`.

Для связи с позициями заказа используется:

```java id="ij50oe"
cascade = CascadeType.ALL
```

Это позволяет сохранить позиции заказа вместе с самим заказом.

### Сущность OrderDetail

```java id="qoegx3"
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    private BigDecimal price;

    public OrderDetail() {
    }

    public OrderDetail(Product product, Integer quantity, BigDecimal price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
```

Сущность `OrderDetail` описывает позицию заказа.
Она связывает заказ и товар.

### Репозитории

В пакете `ru.bsuedu.cad.lab.repository` были реализованы репозитории для каждой сущности.

```java id="grdbvw"
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
```

```java id="rjdjgi"
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
```

```java id="ekfa4t"
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
```

```java id="d7vdzm"
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
```

```java id="kuf5ui"
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
```

Каждый репозиторий наследуется от `JpaRepository`, благодаря чему автоматически получает готовые методы:

* `save()` — сохранение сущности;
* `findById()` — поиск записи по идентификатору;
* `findAll()` — получение всех записей;
* `delete()` — удаление записи.

При запуске приложения Spring Data JPA нашел 5 интерфейсов репозиториев.

### Загрузка данных из CSV

Для заполнения таблиц начальными данными был создан сервис `CsvDataLoader`.

```java id="ym2mz6"
@Service
public class CsvDataLoader {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public CsvDataLoader(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void loadData() {
        loadCategories();
        loadCustomers();
        loadProducts();
    }
}
```

Данный сервис загружает данные из файлов:

* `category.csv`;
* `customer.csv`;
* `product.csv`.

Данные сохраняются в базу через репозитории Spring Data JPA.

### Сервис заказов

В пакете `ru.bsuedu.cad.lab.service` был создан сервис `OrderService`.

```java id="v8pqlv"
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Integer customerId, Map<Integer, Integer> products) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден: " + customerId));

        Order order = new Order(
                customer,
                LocalDateTime.now(),
                "CREATED",
                "Белгород"
        );

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Integer productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар не найден: " + productId));

            BigDecimal detailPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

            OrderDetail detail = new OrderDetail(product, quantity, detailPrice);
            order.addDetail(detail);

            totalPrice = totalPrice.add(detailPrice);
        }

        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        log.info(
                "Создан заказ: id={}, покупатель='{}', сумма={}, позиций={}",
                savedOrder.getOrderId(),
                savedOrder.getCustomer().getName(),
                savedOrder.getTotalPrice(),
                savedOrder.getDetails().size()
        );

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
```

Метод `createOrder()` выполняет создание заказа.
Создание заказа выполняется в рамках транзакции с помощью аннотации `@Transactional`.

Внутри метода:

1. Выполняется поиск покупателя по идентификатору.
2. Выполняется поиск товаров по идентификаторам.
3. Создается заказ.
4. Создаются позиции заказа.
5. Рассчитывается итоговая сумма.
6. Заказ сохраняется в базе данных через `OrderRepository`.

### Клиент приложения

В пакете `ru.bsuedu.cad.lab.app` был создан класс `ShopApplication`.

```java id="gf7x8m"
@Component
public class ShopApplication {

    private static final Logger log = LoggerFactory.getLogger(ShopApplication.class);

    private final CsvDataLoader csvDataLoader;
    private final OrderService orderService;

    public ShopApplication(CsvDataLoader csvDataLoader, OrderService orderService) {
        this.csvDataLoader = csvDataLoader;
        this.orderService = orderService;
    }

    public void run() {
        csvDataLoader.loadData();

        Order order = orderService.createOrder(
                1,
                Map.of(
                        1, 2,
                        8, 1
                )
        );

        log.info("Проверка сохранения заказа в базе данных");

        var orders = orderService.getAllOrders();

        for (Order savedOrder : orders) {
            log.info(
                    "Заказ найден в БД: id={}, сумма={}, количество позиций={}",
                    savedOrder.getOrderId(),
                    savedOrder.getTotalPrice(),
                    savedOrder.getDetails().size()
            );
        }
    }
}
```

Данный класс является клиентом для сервиса заказов.
Он загружает данные из CSV-файлов, создает новый заказ и затем получает список всех заказов из базы данных, чтобы доказать, что заказ был сохранен.

Главный класс приложения `App`:

```java id="s9xxzu"
package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.Config;

public class App {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(Config.class)) {
            ShopApplication application = context.getBean(ShopApplication.class);
            application.run();
        }
    }
}
```

### Результат запуска приложения

Приложение запускается командой:

```bash id="bw3yqy"
gradle run
```

После запуска в консоль выводится информация о старте Spring Data JPA, Hibernate, HikariCP, создании заказа и проверке его сохранения в базе данных.

Пример вывода:

```text id="na6utg"
INFO  RepositoryConfigurationDelegate - Finished Spring Data repository scanning. Found 5 JPA repository interfaces.
INFO  Hibernate ORM core version 6.6.5.Final
INFO  HikariDataSource - HikariPool-1 - Starting...
INFO  HikariDataSource - HikariPool-1 - Start completed.
INFO  OrderService - Создан заказ: id=1, покупатель='Алексей Иванов', сумма=3550.00, позиций=2
INFO  ShopApplication - Проверка сохранения заказа в базе данных
INFO  ShopApplication - Заказ найден в БД: id=1, сумма=3550.00, количество позиций=2
```

## Выводы
В ходе выполнения лабораторной работы были выполнены следующие задачи:
- проведен рефакторинг приложения.
- приложение расширено новыми сущностями
- структуру приложения была приведена в соответствие со "слоистой архитектурой".