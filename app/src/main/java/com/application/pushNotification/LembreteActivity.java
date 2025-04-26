package com.application.pushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class LembreteActivity extends AppCompatActivity {

    private EditText editMessage, editSeconds;
    private Button btnSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete);

        editMessage = findViewById(R.id.editMessage);
        editSeconds = findViewById(R.id.editSeconds);
        btnSetReminder = findViewById(R.id.btnSetReminder);

        createNotificationChannel();

        btnSetReminder.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            String secondsStr = editSeconds.getText().toString();

            if (message.isEmpty() || secondsStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            int seconds = Integer.parseInt(secondsStr);
            startReminderTimer(seconds, message);
        });
    }

    private void startReminderTimer(int seconds, String message) {
        new CountDownTimer(seconds * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                btnSetReminder.setText("Faltam: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                showNotification("Aviso de lembrete", message);
                btnSetReminder.setText("Agendar Lembrete");
            }
        }.start();
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "timer_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
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
