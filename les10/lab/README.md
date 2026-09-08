# Отчет о лабораторной работе №5

# Разработка и развертывание Web-приложений

## Цель работы

Целью лабораторной работы является разработка и развертывание Web-приложения для магазина зоотоваров.

В ходе работы необходимо настроить проект для сборки WAR-файла, развернуть приложение на сервере Apache Tomcat 11, реализовать сервлеты для отображения списка заказов, создания нового заказа и получения информации о продуктах через REST-сервис.

## Выполнение работы

В качестве основы был использован результат выполнения предыдущей лабораторной работы, в которой приложение было переведено на использование JPA, Hibernate и Spring Data.

Проект был скопирован в директорию `/les10/lab/`.

В предыдущей версии приложения уже были реализованы:

* JPA-сущности;
* репозитории Spring Data JPA;
* сервисный слой;
* загрузка начальных данных из CSV-файлов;
* создание заказа в рамках транзакции.

В данной лабораторной работе приложение было дополнено Web-интерфейсом на основе Java Servlet API.

### Установка и настройка Apache Tomcat 11

Для развертывания приложения был скачан и установлен сервер Apache Tomcat 11.

### Настройка сборки WAR-файла

Для сборки приложения в формате WAR был изменен файл `build.gradle.kts`.

В проект был добавлен плагин `war`.

```kotlin
plugins {
    java
    war
}
```

Также были добавлены зависимости, необходимые для работы Spring, JPA, Hibernate, HikariCP, H2, логирования и сервлетов.

```kotlin
dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")
    implementation("org.springframework:spring-tx:6.2.2")
    implementation("org.springframework:spring-web:6.2.2")

    implementation("org.springframework.data:spring-data-jpa:3.4.2")

    implementation("org.hibernate.orm:hibernate-core:6.6.5.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.3.232")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")
}
```

Зависимость `jakarta.servlet-api` подключена как `providedCompile`, так как реализация Servlet API предоставляется сервером Apache Tomcat.

Для задания имени итогового WAR-файла была добавлена настройка:

```kotlin
tasks.war {
    archiveFileName.set("petshop.war")
}
```

Сборка приложения выполняется командой:

```bash
gradle war
```

После выполнения команды формируется WAR-файл:

```text
app/build/libs/petshop.war
```

### Структура проекта

После доработки структура проекта имеет следующий вид:

```text
ru.bsuedu.cad.lab
├── Config.java
├── entity
├── repository
├── service
└── web
```

Назначение пакетов:

* `ru.bsuedu.cad.lab.entity` — JPA-сущности;
* `ru.bsuedu.cad.lab.repository` — репозитории Spring Data JPA;
* `ru.bsuedu.cad.lab.service` — сервисный слой приложения;
* `ru.bsuedu.cad.lab.web` — сервлеты Web-приложения.

Также был создан каталог:

```text
src/main/webapp/WEB-INF
```

В нем расположен файл `web.xml`.

### Конфигурация web.xml

Для настройки Web-приложения был создан файл:

```text
src/main/webapp/WEB-INF/web.xml
```

В данном файле описана загрузка Spring-контекста и регистрация сервлетов.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_6_1.xsd"
         version="6.1">

    <display-name>Pet Shop Web Application</display-name>

    <context-param>
        <param-name>contextClass</param-name>
        <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </context-param>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>ru.bsuedu.cad.lab.Config</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>ordersServlet</servlet-name>
        <servlet-class>ru.bsuedu.cad.lab.web.OrdersServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>ordersServlet</servlet-name>
        <url-pattern>/orders</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>createOrderServlet</servlet-name>
        <servlet-class>ru.bsuedu.cad.lab.web.CreateOrderServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>createOrderServlet</servlet-name>
        <url-pattern>/orders/new</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>productRestServlet</servlet-name>
        <servlet-class>ru.bsuedu.cad.lab.web.ProductRestServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>productRestServlet</servlet-name>
        <url-pattern>/api/products</url-pattern>
    </servlet-mapping>

    <welcome-file-list>
        <welcome-file>orders</welcome-file>
    </welcome-file-list>

</web-app>
```

`ContextLoaderListener` используется для загрузки Spring ApplicationContext при старте Web-приложения.

В качестве класса конфигурации указывается:

```text
ru.bsuedu.cad.lab.Config
```

### Конфигурация Spring-приложения

Класс `Config` отвечает за настройку DataSource, EntityManagerFactory, TransactionManager и репозиториев Spring Data JPA.

```java
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

В приложении используется база данных H2 и пул соединений HikariCP.
Схема базы данных создается автоматически на основании JPA-сущностей.

### Загрузка начальных данных

Так как приложение теперь запускается не через метод `main`, а внутри Tomcat, загрузка данных из CSV-файлов выполняется при инициализации Spring-контекста.

Для этого был создан класс `DataInitializer`.

```java
@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CsvDataLoader csvDataLoader;

    public DataInitializer(CsvDataLoader csvDataLoader) {
        this.csvDataLoader = csvDataLoader;
    }

    @PostConstruct
    public void init() {
        csvDataLoader.loadData();
        log.info("Начальные данные загружены из CSV-файлов");
    }
}
```

При старте Web-приложения данный компонент вызывает сервис `CsvDataLoader`, который загружает данные из файлов:

* `category.csv`;
* `customer.csv`;
* `product.csv`.

### Реализация сервлета OrdersServlet

Для отображения списка заказов был создан сервлет `OrdersServlet`.

```java
public class OrdersServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Order> orders = orderService.getAllOrders();

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");

        var out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Заказы</title>
                </head>
                <body>
                    <h1>Список заказов</h1>
                    <a href="orders/new">Создать заказ</a>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Покупатель</th>
                                <th>Дата</th>
                                <th>Статус</th>
                                <th>Сумма</th>
                                <th>Адрес доставки</th>
                                <th>Позиций</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        for (Order order : orders) {
            out.printf("""
                            <tr>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%d</td>
                            </tr>
                    """,
                    order.getOrderId(),
                    order.getCustomer().getName(),
                    order.getOrderDate(),
                    order.getStatus(),
                    order.getTotalPrice(),
                    order.getShippingAddress(),
                    order.getDetails().size()
            );
        }

        out.println("""
                        </tbody>
                    </table>
                </body>
                </html>
                """);
    }
}
```

В методе `init()` сервлет получает Spring ApplicationContext из `ServletContext`, а затем получает бин `OrderService`.

Сервлет формирует HTML-страницу со списком заказов.
Также на странице есть ссылка для перехода к форме создания заказа.

Адрес страницы:

```text
http://localhost:8080/petshop/orders
```

### Реализация сервлета CreateOrderServlet

Для создания нового заказа был реализован сервлет `CreateOrderServlet`.

```java
public class CreateOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");

        var out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Создание заказа</title>
                </head>
                <body>
                    <h1>Создание заказа</h1>

                    <form method="post" action="new">
                        <label>Id покупателя</label>
                        <input type="number" name="customerId" value="1" required>

                        <label>Id товара</label>
                        <input type="number" name="productId" value="1" required>

                        <label>Количество</label>
                        <input type="number" name="quantity" value="1" required>

                        <button type="submit">Создать заказ</button>
                    </form>

                    <p><a href="../orders">Вернуться к списку заказов</a></p>
                </body>
                </html>
                """);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Integer customerId = Integer.parseInt(request.getParameter("customerId"));
        Integer productId = Integer.parseInt(request.getParameter("productId"));
        Integer quantity = Integer.parseInt(request.getParameter("quantity"));

        orderService.createOrder(customerId, Map.of(productId, quantity));

        response.sendRedirect(request.getContextPath() + "/orders");
    }
}
```

Метод `doGet()` формирует HTML-форму создания заказа.

Метод `doPost()` получает параметры формы, вызывает сервис создания заказа и после успешного создания перенаправляет пользователя на список заказов.

Адрес формы:

```text
http://localhost:8080/petshop/orders/new
```

### Реализация REST-сервиса ProductRestServlet

Для получения информации о продуктах был создан сервлет `ProductRestServlet`.

```java
public class ProductRestServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        productService = context.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = productService.getAllProducts();

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json; charset=UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            json.append("{")
                    .append("\"name\":\"").append(escapeJson(product.getName())).append("\",")
                    .append("\"category\":\"").append(escapeJson(product.getCategory().getName())).append("\",")
                    .append("\"stockQuantity\":").append(product.getStockQuantity())
                    .append("}");

            if (i < products.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        response.getWriter().write(json.toString());
    }
}
```

Сервлет возвращает информацию о продуктах в формате JSON.

Для каждого продукта выводится:

* название продукта;
* название категории;
* количество на складе.


### Сервис ProductService

Для получения списка товаров был создан сервис `ProductService`.

```java
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
```

Данный сервис используется REST-сервлетом для получения списка товаров из базы данных.

### Сервис OrderService

Для создания заказов и получения списка заказов используется сервис `OrderService`.

```java
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

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

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
```

Создание заказа выполняется в рамках транзакции с помощью аннотации `@Transactional`.

### Деплой приложения в Tomcat

После сборки приложения командой:

```bash
gradle war
```

был получен WAR-файл:

```text
petshop.war
```

Файл был помещен в директорию Tomcat:

```text
apache-tomcat-11/webapps
```

После запуска Tomcat приложение было автоматически развернуто и стало доступно по адресу:

```text
http://localhost:8080/petshop/orders
```

## Выводы

В ходе выполнения лабораторной работы было разработано Web-приложение для магазина зоотоваров.

Проект был настроен для сборки WAR-файла и последующего развертывания на сервере Apache Tomcat 11. Для интеграции сервлетов со Spring был использован `ContextLoaderListener`, который загружает Spring ApplicationContext при запуске Web-приложения. В результате приложение было успешно собрано командой `gradle war`.
