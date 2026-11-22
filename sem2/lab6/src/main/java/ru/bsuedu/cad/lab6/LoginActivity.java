package ru.bsuedu.cad.lab6;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.nio.charset.StandardCharsets;
import java.util.List;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final TaskApi API = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi.class);

    private Call<List<Task>> pending;
    private EditText username;
    private EditText password;
    private TextView error;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogin();
    }

    @Override
    protected void onDestroy() {
        if (pending != null) {
            pending.cancel();
        }
        super.onDestroy();
    }

    private void showLogin() {
        setContentView(R.layout.activity_login);
        applyInsets();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        error = findViewById(R.id.error);
        login = findViewById(R.id.login);
        login.setOnClickListener(view -> loadTasks());
    }

    private void loadTasks() {
        String name = username.getText().toString().trim();
        String secret = password.getText().toString();
        if (name.isEmpty() || secret.isEmpty()) {
            error.setText(R.string.enter_credentials);
            return;
        }

        login.setEnabled(false);
        error.setText("");
        getSystemService(InputMethodManager.class).hideSoftInputFromWindow(password.getWindowToken(), 0);
        pending = API.getTasks(Credentials.basic(name, secret, StandardCharsets.UTF_8));
        pending.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (call.isCanceled()) {
                    return;
                }
                pending = null;
                var tasks = response.body();
                if (response.isSuccessful() && tasks != null) {
                    password.setText("");
                    showTasks(tasks);
                } else {
                    showError(response.code() == 401 ? R.string.invalid_credentials : R.string.load_error);
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable cause) {
                if (!call.isCanceled()) {
                    pending = null;
                    showError(R.string.connection_error);
                }
            }
        });
    }

    private void showError(int message) {
        login.setEnabled(true);
        error.setText(message);
    }

    private void showTasks(List<Task> tasks) {
        setContentView(R.layout.activity_tasks);
        applyInsets();
        findViewById(R.id.logout).setOnClickListener(view -> showLogin());
        findViewById(R.id.empty).setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
        RecyclerView list = findViewById(R.id.tasks);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new TaskAdapter(tasks));
    }

    private void applyInsets() {
        View content = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(content, (view, insets) -> {
            var bounds = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            view.setPadding(bounds.left, bounds.top, bounds.right, bounds.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(content);
    }
}
