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


public class TimerActivity extends AppCompatActivity {

    private EditText editSeconds;
    private Button btnStartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        editSeconds = findViewById(R.id.editSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);

        createNotificationChannel();

        btnStartTimer.setOnClickListener(v -> {
            String secondsStr = editSeconds.getText().toString();
            if (!secondsStr.isEmpty()) {
                int seconds = Integer.parseInt(secondsStr);
                startTimer(seconds);
            } else {
                Toast.makeText(this, "Digite os segundos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer(int seconds) {
        new CountDownTimer(seconds * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                btnStartTimer.setText("Faltam: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                showNotification("Cronômetro finalizado", "O tempo acabou!");
                btnStartTimer.setText("Iniciar");
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
        manager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal do Timer";
            String description = "Notificações de término do cronômetro";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("timer_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}


