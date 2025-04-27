package com.application.pushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LembreteActivity extends AppCompatActivity {

    private EditText editMessage;
    private TimePicker timePicker;
    private Button btnSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete);

        editMessage = findViewById(R.id.editMessage);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        btnSetReminder = findViewById(R.id.btnSetReminder);

        createNotificationChannel();

        btnSetReminder.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (!message.isEmpty()) {
                long millisUntilReminder = calculateMillisUntil(hour, minute);

                // 👉 Iniciar o LembreteService
                Intent serviceIntent = new Intent(this, LembreteService.class);
                serviceIntent.putExtra(LembreteService.EXTRA_MILLIS, millisUntilReminder);
                serviceIntent.putExtra(LembreteService.EXTRA_MESSAGE, message);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                // Voltar para MainActivity depois de agendar
                Intent intent = new Intent(LembreteActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // fecha o LembreteActivity para não ficar empilhado
            } else {
                Toast.makeText(this, "Digite uma mensagem para o lembrete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private long calculateMillisUntil(int hour, int minute) {
        Calendar now = Calendar.getInstance();
        Calendar reminderTime = (Calendar) now.clone();
        reminderTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderTime.set(Calendar.MINUTE, minute);
        reminderTime.set(Calendar.SECOND, 0);
        reminderTime.set(Calendar.MILLISECOND, 0);

        if (reminderTime.before(now)) {
            // Se o horário for antes do agora, significa que é para o dia seguinte
            reminderTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        return reminderTime.getTimeInMillis() - now.getTimeInMillis();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal do Timer";
            String description = "Notificações de lembretes";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("timer_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}