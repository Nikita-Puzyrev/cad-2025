package com.musicstore.android;

import com.musicstore.android.models.Product;
import com.musicstore.android.models.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Для эмулятора Android
    // private static final String BASE_URL = "http://192.168.1.X:8080/"; // Для реального устройства
    
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    
    public interface ApiService {
        // Товары
        @GET("api/products")
        Call<List<Product>> getAllProducts();
        
        @GET("api/products/{id}")
        Call<Product> getProductById(@Path("id") Long id);
        
        @POST("api/products")
        Call<Product> createProduct(@Body Product product);
        
        @PUT("api/products/{id}")
        Call<Product> updateProduct(@Path("id") Long id, @Body Product product);
        
        @DELETE("api/products/{id}")
        Call<Void> deleteProduct(@Path("id") Long id);
        
        // Заказы
        @GET("api/orders")
        Call<List<Order>> getAllOrders();
        
        // Проверка подключения
        @GET("actuator/health")
        Call<Void> checkHealth();
    }
    
    public static ApiService getApiService() {
        if (apiService == null) {
            // Настройка логирования
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Настройка клиента
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();
            
            // Настройка Gson
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            
            // Создание Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
    
    // Проверка подключения
    public static void checkConnection(ConnectionCallback callback) {
        getApiService().checkHealth().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess(response.isSuccessful());
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
    
    public interface ConnectionCallback {
        void onSuccess(boolean isConnected);
        void onFailure(Throwable t);
    }
    
    public interface ProductsCallback {
        void onSuccess(List<Product> products);
        void onFailure(Throwable t);
    }
    
    public interface ProductCallback {
        void onSuccess(Product product);
        void onFailure(Throwable t);
    }
    
    public interface BooleanCallback {
        void onSuccess(boolean success);
        void onFailure(Throwable t);
    }
}