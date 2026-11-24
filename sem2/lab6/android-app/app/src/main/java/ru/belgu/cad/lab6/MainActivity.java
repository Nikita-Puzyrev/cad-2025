package ru.belgu.cad.lab6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Главная активность приложения, созданного по шаблону Empty Views Activity.
 * Проверяет работоспособность проекта: по нажатию кнопки счётчик увеличивается.
 */
public class MainActivity extends AppCompatActivity {

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView counterView = findViewById(R.id.counterText);
        TextView sdkView = findViewById(R.id.sdkText);
        Button button = findViewById(R.id.countButton);

        sdkView.setText(getString(R.string.sdk_info,
                android.os.Build.VERSION.SDK_INT, android.os.Build.MODEL));

        button.setOnClickListener(v -> {
            counter++;
            counterView.setText(getString(R.string.counter_value, counter));
        });
    }
}
