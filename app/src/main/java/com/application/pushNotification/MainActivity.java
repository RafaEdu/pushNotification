package com.application.pushNotification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Botões da tela principal
    Button btnAbrirTimer;
    Button btnAbrirLembrete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Define o layout principal da aplicação

        // Ligando os botões do XML às variáveis Java
        btnAbrirTimer = findViewById(R.id.btnAbrirTimer);
        btnAbrirLembrete = findViewById(R.id.btnAbrirLembrete);

        // Ação ao clicar no botão "Abrir Timer"
        btnAbrirTimer.setOnClickListener(v -> {
            // Cria uma intenção para abrir a tela do cronômetro (TimerActivity)
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
        });

        // Ação ao clicar no botão "Abrir Lembrete"
        btnAbrirLembrete.setOnClickListener(v -> {
            // Cria uma intenção para abrir a tela de lembrete (LembreteActivity)
            Intent intent = new Intent(this, LembreteActivity.class);
            startActivity(intent);
        });
    }
}
