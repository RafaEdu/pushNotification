package com.application.pushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LembreteService extends Service {

    public static final String CHANNEL_ID = "ReminderServiceChannel";
    public static final String EXTRA_MILLIS = "extra_millis";
    public static final String EXTRA_MESSAGE = "extra_message";

    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long millisUntilReminder = intent.getLongExtra(EXTRA_MILLIS, 0);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        countDownTimer = new CountDownTimer(millisUntilReminder, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Optional: update notification if you want
            }

            @Override
            public void onFinish() {
                showReminderNotification(message);
                stopSelf();
            }
        }.start();

        return START_NOT_STICKY;
    }

    private void showReminderNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lembrete")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(2, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Reminder Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
