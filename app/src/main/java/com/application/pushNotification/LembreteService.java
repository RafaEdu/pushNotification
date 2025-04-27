// Esta classe é responsável por gerenciar o serviço de notificações do lembrete.
// Foi implementado para que o serviço de notificação continuasse rodando mesmo após o aplicativo ser fechado.

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

    // ID do canal de notificação
    public static final String CHANNEL_ID = "ReminderServiceChannel";
    // Chave usada para pegar o tempo em milissegundos e a mensagem enviado via Intent
    public static final String EXTRA_MILLIS = "extra_millis";
    public static final String EXTRA_MESSAGE = "extra_message";

    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    // Função para ser executada quando o serviço for iniciado
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Pega o tempo até o lembrete e a mensagem
        long millisUntilReminder = intent.getLongExtra(EXTRA_MILLIS, 0);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String timeFormatted = formatMilissecondsToTime(millisUntilReminder);

        startForeground(1, getNotification(message, timeFormatted));

        // Cria um timer
        countDownTimer = new CountDownTimer(millisUntilReminder, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Aqui poderia implementar uma lógica para ser realizada a cada segundo
            }

            @Override
            public void onFinish() {
                // Quando o tempo acaba, exibe a notificação e para o serviço
                showReminderNotification(message);
                stopSelf();
            }
        }.start();

        // Se o serviço for encerrado pelo sistema, não precisa recriá-lo
        return START_NOT_STICKY;
    }

    // Função para ser executada quando a notificação ter que ser exibida
    private void showReminderNotification(String message) {
        // Obtém o gerenciador de notificações do sistema
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Cria a notificação usando NotificationCompat (bom para Android 7.1 e anteriores)
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lembrete")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        // Exibe a notificação
        notificationManager.notify(2, notification);
    }

    // Função para criar um canal de notificação (é obrigatório para Android 8.0 e superiores)
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

    // Função para criar a notificação inicial
    public Notification getNotification(String message, String timeFormatted) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lembrete")
                .setContentText(message + " foi definido para daqui " + timeFormatted)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        return notification;
    }

    // Função para formatar o tempo de milissegundos para o formato HH:MM:SS
    public String formatMilissecondsToTime(long millisUntilFinished) {
        int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
        int minutes = (int) ((millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((millisUntilFinished % (1000 * 60)) / 1000);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Método obrigatório
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Função para quando o serviço é destruído
    @Override
    public void onDestroy() {
        // Se ele ainda estiver rodando mesmo após o serviço ser destruído, cancela o timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
