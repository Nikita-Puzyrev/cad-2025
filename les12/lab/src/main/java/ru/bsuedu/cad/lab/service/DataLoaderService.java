package ru.bsuedu.cad.lab.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataLoaderService {

    private final CategoryRepository categoryRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    public DataLoaderService(CategoryRepository categoryRepo,
                             CustomerRepository customerRepo,
                             ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public void loadAll() throws Exception {

        Map<Integer, Category> categoryMap = new HashMap<>();

        // ===== CATEGORY =====
        Iterable<CSVRecord> catRecords =
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .parse(new InputStreamReader(
                                getClass().getClassLoader().getResourceAsStream("category.csv")));

        for (CSVRecord r : catRecords) {
            Category c = new Category();
            c.setId(Integer.parseInt(r.get("category_id")));
            c.setName(r.get("name"));
            c.setDescription(r.get("description"));

            categoryRepo.save(c);
            categoryMap.put(c.getId(), c);
        }

        // ===== CUSTOMER =====
        Iterable<CSVRecord> custRecords =
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .parse(new InputStreamReader(
                                getClass().getClassLoader().getResourceAsStream("customer.csv")));

        for (CSVRecord r : custRecords) {
            Customer c = new Customer();
            c.setId(Integer.parseInt(r.get("customer_id")));
            c.setName(r.get("name"));
            c.setEmail(r.get("email"));
            c.setPhone(r.get("phone"));
            c.setAddress(r.get("address"));

            customerRepo.save(c);
        }

        // ===== PRODUCT =====
        Iterable<CSVRecord> prodRecords =
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .parse(new InputStreamReader(
                                getClass().getClassLoader().getResourceAsStream("product.csv")));

        for (CSVRecord r : prodRecords) {
            Product p = new Product();
            p.setId(Integer.parseInt(r.get("product_id")));
            p.setName(r.get("name"));
            p.setDescription(r.get("description"));
            p.setCategory(categoryMap.get(Integer.parseInt(r.get("category_id"))));
            p.setPrice(new BigDecimal(r.get("price")));
            p.setStockQuantity(Integer.parseInt(r.get("stock_quantity")));
            p.setImageUrl(r.get("image_url"));

            p.setCreatedAt(LocalDate.parse(r.get("created_at")).atStartOfDay());
            p.setUpdatedAt(LocalDate.parse(r.get("updated_at")).atStartOfDay());

            productRepo.save(p);
        }
    }
}