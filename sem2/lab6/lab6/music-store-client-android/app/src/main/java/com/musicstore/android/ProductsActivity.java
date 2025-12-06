package com.musicstore.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.musicstore.android.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity 
        implements ProductAdapter.OnItemClickListener {
    
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabAdd;
    private ProgressDialog progressDialog;
    
    private List<Product> productList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        
        setupViews();
        setupRecyclerView();
        loadProducts();
        
        swipeRefreshLayout.setOnRefreshListener(this::loadProducts);
        fabAdd.setOnClickListener(v -> showAddProductDialog());
    }
    
    private void setupViews() {
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        fabAdd = findViewById(R.id.fab_add);
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Загрузка...");
        progressDialog.setCancelable(false);
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);
    }
    
    private void loadProducts() {
        swipeRefreshLayout.setRefreshing(true);
        
        ApiClient.getApiService().getAllProducts().enqueue(new retrofit2.Callback<List<Product>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Product>> call, 
                                   retrofit2.Response<List<Product>> response) {
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.updateData(productList);
                    
                    if (productList.isEmpty()) {
                        Toast.makeText(ProductsActivity.this, 
                            "Товары не найдены", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductsActivity.this, 
                        "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ProductsActivity.this, 
                    "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void onItemClick(Product product) {
        // Показать детали товара
        showProductDetailDialog(product);
    }
    
    @Override
    public void onEditClick(Product product) {
        showEditProductDialog(product);
    }
    
    @Override
    public void onDeleteClick(Product product) {
        showDeleteConfirmDialog(product);
    }
    
    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить товар");
        
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);
        
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etPrice = dialogView.findViewById(R.id.et_price);
        EditText etStock = dialogView.findViewById(R.id.et_stock);
        EditText etCategory = dialogView.findViewById(R.id.et_category);
        
        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String stockStr = etStock.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            
            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double price = Double.parseDouble(priceStr);
                int stock = stockStr.isEmpty() ? 1 : Integer.parseInt(stockStr);
                
                Product newProduct = new Product(name, description, price, stock, category);
                createProduct(newProduct);
                
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Неверный формат числа", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Отмена", null);
        
        builder.show();
    }
    
    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Редактировать товар");
        
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);
        
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etPrice = dialogView.findViewById(R.id.et_price);
        EditText etStock = dialogView.findViewById(R.id.et_stock);
        EditText etCategory = dialogView.findViewById(R.id.et_category);
        
        // Заполняем существующими данными
        etName.setText(product.getName());
        etDescription.setText(product.getDescription());
        etPrice.setText(String.valueOf(product.getPrice()));
        etStock.setText(String.valueOf(product.getStockQuantity()));
        etCategory.setText(product.getCategory());
        
        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String stockStr = etStock.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            
            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double price = Double.parseDouble(priceStr);
                int stock = stockStr.isEmpty() ? 1 : Integer.parseInt(stockStr);
                
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setStockQuantity(stock);
                product.setCategory(category);
                
                updateProduct(product);
                
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Неверный формат числа", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Отмена", null);
        
        builder.show();
    }
    
    private void showProductDetailDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(product.getName());
        
        String message = String.format(
            "Цена: %s\n" +
            "На складе: %s\n" +
            "Категория: %s\n\n" +
            "Описание:\n%s",
            product.getFormattedPrice(),
            product.getFormattedStock(),
            product.getCategoryName(),
            product.getDescription() != null ? product.getDescription() : "Нет описания"
        );
        
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        
        builder.show();
    }
    
    private void showDeleteConfirmDialog(Product product) {
        new AlertDialog.Builder(this)
            .setTitle("Удаление")
            .setMessage("Удалить товар \"" + product.getName() + "\"?")
            .setPositiveButton("Да", (dialog, which) -> deleteProduct(product))
            .setNegativeButton("Нет", null)
            .show();
    }
    
    private void createProduct(Product product) {
        progressDialog.show();
        
        ApiClient.getApiService().createProduct(product).enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(retrofit2.Call<Product> call, 
                                   retrofit2.Response<Product> response) {
                progressDialog.dismiss();
                
                if (response.isSuccessful()) {
                    Toast.makeText(ProductsActivity.this, 
                        "Товар создан", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductsActivity.this, 
                        "Ошибка создания", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(retrofit2.Call<Product> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductsActivity.this, 
                    "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void updateProduct(Product product) {
        progressDialog.show();
        
        ApiClient.getApiService().updateProduct(product.getId(), product)
                .enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(retrofit2.Call<Product> call, 
                                   retrofit2.Response<Product> response) {
                progressDialog.dismiss();
                
                if (response.isSuccessful()) {
                    Toast.makeText(ProductsActivity.this, 
                        "Товар обновлен", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductsActivity.this, 
                        "Ошибка обновления", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(retrofit2.Call<Product> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductsActivity.this, 
                    "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void deleteProduct(Product product) {
        progressDialog.show();
        
        ApiClient.getApiService().deleteProduct(product.getId())
                .enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, 
                                   retrofit2.Response<Void> response) {
                progressDialog.dismiss();
                
                if (response.isSuccessful()) {
                    Toast.makeText(ProductsActivity.this, 
                        "Товар удален", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductsActivity.this, 
                        "Ошибка удаления", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductsActivity.this, 
                    "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}