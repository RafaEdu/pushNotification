package com.application.pushNotification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private EditText editSeconds;
    private Button btnStartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        editSeconds = findViewById(R.id.editSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);

        btnStartTimer.setOnClickListener(v -> {
            String secondsStr = editSeconds.getText().toString();
            if (!secondsStr.isEmpty()) {
                try {
                    int seconds = Integer.parseInt(secondsStr);
                    if (seconds > 0) {
                        Intent serviceIntent = new Intent(this, TimerService.class);
                        serviceIntent.putExtra(TimerService.EXTRA_SECONDS, seconds);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        Toast.makeText(this, "Cronômetro iniciado para " + seconds + " segundos!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Digite um número maior que zero!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Digite um número válido!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Digite os segundos!", Toast.LENGTH_SHORT).show();
            }
            editSeconds.setText("");
        });
    }
}