// Esta classe é responsável por gerenciar um serviço de cronômetro.
// Permite iniciar um cronômetro em segundo plano que atualiza a notificação a cada segundo e informa quando o tempo acabar.

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

    // ID do canal de notificação
    public static final String CHANNEL_ID = "TimerServiceChannel";
    // Chave usada para pegar o tempo em segundos enviado via Intent
    public static final String EXTRA_SECONDS = "extra_seconds";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    // Função chamada quando o serviço é iniciado
    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Pega o tempo total em segundos enviado via Intent
        int seconds = intent.getIntExtra(EXTRA_SECONDS, 0);

        // Inicia o serviço como um serviço de primeiro plano (obrigatório para serviços contínuos no Android)
        startForeground(1, getNotification("Timer iniciado"));

        // Cria um temporizador que irá contar de segundo em segundo
        new CountDownTimer(seconds * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                // Atualiza a notificação com o tempo restante a cada segundo
                int remainingSeconds = (int) (millisUntilFinished / 1000);
                updateNotification(remainingSeconds);
            }

            public void onFinish() {
                // Quando o cronômetro acaba, exibe a notificação de finalização e para o serviço
                showFinishNotification();
                stopSelf();
            }
        }.start();

        // Se o serviço for encerrado pelo sistema, não precisa recriá-lo
        return START_NOT_STICKY;
    }

    // Função para atualizar a notificação com o tempo restante
    private void updateNotification(int remainingSeconds) {
        String timeFormatted = formatSecondsToTime(remainingSeconds);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cronômetro")
                .setContentText("Tempo restante: " + timeFormatted)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    // Função para formatar o tempo de segundos para o formato HH:MM:SS
    private String formatSecondsToTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Função para criar a notificação inicial
    private Notification getNotification(String message) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cronômetro")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .build();
    }


    // Função para mostrar uma notificação quando o cronômetro terminar
    private void showFinishNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cronômetro finalizado")
                .setContentText("O tempo acabou!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(2, notification);
    }

    // Função para criar o canal de notificação (obrigatório para Android 8.0 ou superior)
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

    // Método obrigatório
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
