package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

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

    private void loadCategories() {
        String text = readResource("category.csv");
        String[] lines = text.split("\\R");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(",", 3);

            Category category = new Category(
                    Integer.parseInt(values[0].trim()),
                    values[1].trim(),
                    values[2].trim()
            );

            categoryRepository.save(category);
        }
    }

    private void loadCustomers() {
        String text = readResource("customer.csv");
        String[] lines = text.split("\\R");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(",", 5);

            Customer customer = new Customer(
                    Integer.parseInt(values[0].trim()),
                    values[1].trim(),
                    values[2].trim(),
                    values[3].trim(),
                    values[4].trim()
            );

            customerRepository.save(customer);
        }
    }

    private void loadProducts() {
        String text = readResource("product.csv");
        String[] lines = text.split("\\R");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(",", -1);

            Category category = categoryRepository.findById(Integer.parseInt(values[3].trim()))
                    .orElseThrow(() -> new RuntimeException("Категория не найдена: " + values[3]));

            Product product = new Product(
                    Integer.parseInt(values[0].trim()),
                    values[1].trim(),
                    values[2].trim(),
                    category,
                    new BigDecimal(values[4].trim()),
                    Integer.parseInt(values[5].trim()),
                    values[6].trim(),
                    LocalDate.parse(values[7].trim()).atStartOfDay(),
                    LocalDate.parse(values[8].trim()).atStartOfDay()
            );

            productRepository.save(product);
        }
    }

    private String readResource(String fileName) {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл не найден: " + fileName);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла: " + fileName, e);
        }
    }
}