package com.fitnessclub.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fitnessclub.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnMembers.setOnClickListener {
            startActivity(Intent(this, MembersActivity::class.java))
        }
        
        binding.btnWorkouts.setOnClickListener {
            startActivity(Intent(this, WorkoutsActivity::class.java))
        }
    }
}package com.musicstore.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private Button btnProducts, btnOrders, btnSettings, btnExit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupClickListeners();
        checkServerConnection();
    }
    
    private void initializeViews() {
        btnProducts = findViewById(R.id.btn_products);
        btnOrders = findViewById(R.id.btn_orders);
        btnSettings = findViewById(R.id.btn_settings);
        btnExit = findViewById(R.id.btn_exit);
    }
    
    private void setupClickListeners() {
        btnProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
            startActivity(intent);
        });
        
        btnOrders.setOnClickListener(v -> {
            Toast.makeText(this, "Заказы - в разработке", Toast.LENGTH_SHORT).show();
        });
        
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Настройки - в разработке", Toast.LENGTH_SHORT).show();
        });
        
        btnExit.setOnClickListener(v -> {
            finish();
        });
    }
    
    private void checkServerConnection() {
        ApiClient.checkConnection(new ApiClient.ConnectionCallback() {
            @Override
            public void onSuccess(boolean isConnected) {
                runOnUiThread(() -> {
                    if (isConnected) {
                        Toast.makeText(MainActivity.this, 
                            "Соединение установлено", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, 
                            "Сервер недоступен", Toast.LENGTH_LONG).show();
                    }
                });
            }
            
            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, 
                        "Ошибка подключения: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}