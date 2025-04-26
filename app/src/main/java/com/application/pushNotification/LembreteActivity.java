package com.application.pushNotification;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

public class LembreteActivity extends AppCompatActivity {

    private EditText editMessage, editSeconds;
    private Button btnSetReminder;
    private Handler handler = new Handler(); // handler para o atraso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete);

        editMessage = findViewById(R.id.editMessage);
        editSeconds = findViewById(R.id.editSeconds);
        btnSetReminder = findViewById(R.id.btnSetReminder);
        
        btnSetReminder.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            String secondsStr = editSeconds.getText().toString();

            if (message.isEmpty() || secondsStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            int seconds = Integer.parseInt(secondsStr);

            Toast.makeText(this, "Lembrete agendado para " + seconds + "s", Toast.LENGTH_SHORT).show();

            handler.postDelayed(() -> {
                Toast.makeText(this, "Lembrete: " + message, Toast.LENGTH_LONG).show();
            }, seconds * 1000L); // milissegundos
        });
    }
}
