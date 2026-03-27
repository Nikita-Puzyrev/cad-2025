Лабораторная работа 1. Gradle. Базовое приложение Spring



\## 1. Цель работы



создать каркас приложения и разобраться с конфигурированием Spring приложений на основе Java-классов.



\## 2. Структура проекта



&nbsp;   src/main/java/ru/bsuedu/cad/lab/

&nbsp;   

&nbsp;   - AppWithSpringJava.java        # Точка входа

&nbsp;   - AppConfig.java                # Java‑конфигурация Spring

&nbsp;   

&nbsp;   - product/

&nbsp;   	- Product.java              # Модель данных

&nbsp;   

&nbsp;   - reader/

&nbsp;      - Reader.java               # Интерфейс

&nbsp;      - ResourceFileReader.java   # Чтение CSV из ресурсов

&nbsp;   

&nbsp;   - parser/

&nbsp;       - Parser.java               # Интерфейс

&nbsp;       - CSVParser.java            # CSV‑парсер

&nbsp;   

&nbsp;   - provider/

&nbsp;       - ProductProvider.java          # Интерфейс

&nbsp;       - ConcreteProductProvider.java  # Реализация

&nbsp;   

&nbsp;   - renderer/

&nbsp;       - Renderer.java             # Интерфейс

&nbsp;       - ConsoleTableRenderer.java # Табличный вывод



\## 3. CSV‑файл



CSV-файл 'products.csv' находится в:



&nbsp;   src/main/resources/products.csv



Формат файла:



&nbsp;   productId,name,description,categoryId,price,stockQuantity,imageUrl,createdAt,updatedAt

&nbsp;   1,Cat Food,Premium cat food,1,12.50,30,cat\_food.jpg,2020-01-10,2020-02-01

&nbsp;   2,Dog Toy,Rubber dog toy,2,5.99,100,dog\_toy.jpg,2020-03-15,2020-04-01



\## 4. Основные компоненты



\### 4.1. Reader / ResourceFileReader



Отвечает за чтение CSV-файла из ресурсов.



\### 4.2. Parser / CSVParser



Конвертирует текст CSV в список объектов 'Product'.



\### 4.3. ProductProvider / ConcreteProductProvider



Связывает Reader и Parser: читает - парсит - отдаёт список продуктов.



\### 4.4. ConsoleTableRenderer



Получает список продуктов и выводит их в консоль в виде таблицы с

фиксированной шириной столбцов.



\## 5. Запуск приложения



\### Через Gradle:



&nbsp;   gradle run



\### Через IntelliJ IDEA:



1\.  Открыть меню \*\*Gradle - Tasks - application - run\*\*

2\.  Нажать \*\*Run\*\*



\## 6. Пример вывода



&nbsp;   +------------+------------------+-------------------------+----------+------------+

&nbsp;   | Product ID | Name             | Description             | Price    | In stock   |

&nbsp;   +------------+------------------+-------------------------+----------+------------+

&nbsp;   | 1          | Cat Food         | Premium cat food        | 12.50    | 30         |

&nbsp;   | 2          | Dog Toy          | Rubber dog toy          | 5.99     | 100        |

&nbsp;   +------------+------------------+-------------------------+----------+------------+



\## 7. Результаты



В ходе работы было создано: - Spring‑приложение с Java‑конфигурацией; -

CSV‑ридер и парсер; - вывод данных в табличном формате; - структура

проекта строго соответствует UML‑диаграмме.



Приложение успешно запускается командой 'gradle run' и корректно

завершает работу.



