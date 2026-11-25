package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataImportService(CategoryRepository categoryRepository,
                             ProductRepository productRepository,
                             CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void importAllData() {
        importCategories();
        importProducts();
        importCustomers();
        System.out.println("Данные из CSV успешно загружены в базу данных H2.");
    }

    private void importCategories() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/category.csv"), StandardCharsets.UTF_8))) {
            String line; boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    categoryRepository.save(new Category(
                            Integer.parseInt(parts[0].trim()), parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void importProducts() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/product.csv"), StandardCharsets.UTF_8))) {
            String line; boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                if (line.trim().isEmpty()) continue;


                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 9) {
                    Category cat = categoryRepository.findById(Integer.parseInt(parts[3].trim())).orElse(null);
                    Product product = new Product();
                    product.setProductId(Integer.parseInt(parts[0].trim()));
                    product.setName(parts[1].replace("\"", "").trim());
                    product.setDescription(parts[2].replace("\"", "").trim());
                    product.setCategory(cat);
                    product.setPrice(new BigDecimal(parts[4].trim()));
                    product.setStockQuantity(Integer.parseInt(parts[5].trim()));
                    product.setImageUrl(parts[6].trim());
                    product.setCreatedAt(LocalDateTime.parse(parts[7].trim() + "T00:00:00"));
                    product.setUpdatedAt(LocalDateTime.parse(parts[8].trim() + "T00:00:00"));
                    productRepository.save(product);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void importCustomers() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/customer.csv"), StandardCharsets.UTF_8))) {
            String line; boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                if (line.trim().isEmpty()) continue;

                // Разделение на 5 частей
                String[] parts = line.split(",", 5);
                if (parts.length >= 5) {
                    Customer customer = new Customer();
                    customer.setCustomerId(Integer.parseInt(parts[0].trim()));
                    customer.setName(parts[1].trim());
                    customer.setEmail(parts[2].trim());
                    customer.setPhone(parts[3].trim());
                    customer.setAddress(parts[4].trim()); // Адрес с запятыми
                    customerRepository.save(customer);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}