package com.application.pushNotification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAbrirTimer;
    Button btnAbrirLembrete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbrirTimer = findViewById(R.id.btnAbrirTimer);
        btnAbrirLembrete = findViewById(R.id.btnAbrirLembrete);

        btnAbrirTimer.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
        });
    }
}
