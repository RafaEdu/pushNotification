package com.application.pushNotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TimerService extends Service {

    public static final String CHANNEL_ID = "TimerServiceChannel";
    public static final String EXTRA_SECONDS = "extra_seconds";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TimerService", "Service created!");
        createNotificationChannel();
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int seconds = intent.getIntExtra(EXTRA_SECONDS, 0);

        Log.d("TimerService", "Service started with timer for " + seconds + " seconds.");

        // Show the notification while the timer is running
        startForeground(1, getNotification("Cronômetro iniciado"));

        new CountDownTimer(seconds * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the notification during the countdown
                int remainingSeconds = (int) (millisUntilFinished / 1000);
                Log.d("TimerService", "Timer ticking, " + remainingSeconds + " seconds remaining.");
                updateNotification(remainingSeconds);  // Update notification with remaining time
            }

            public void onFinish() {
                Log.d("TimerService", "Timer finished!");
                showFinishNotification();
                stopSelf();  // Stop the service after the timer finishes
            }
        }.start();

        return START_NOT_STICKY;
    }

    private void updateNotification(int remainingSeconds) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer")
                .setContentText("Remaining time: " + remainingSeconds + " seconds")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);  // Update the notification with remaining time
    }

    private Notification getNotification(String message) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();
    }

    private void showFinishNotification() {
        Log.d("TimerService", "Showing finish notification...");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cronômetro finalizado")
                .setContentText("O tempo acabou!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(2, notification);  // Use a unique ID for the finish notification
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timer Service Channel",
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
}
