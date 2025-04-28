package com.application.pushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Método chamado automaticamente sempre que o app recebe uma mensagem FCM (Firebase Cloud Messaging)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Verifica se a mensagem contém uma notificação (título e corpo)
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.d("FCM", "Notificação recebida - Título: " + title + ", Corpo: " + body);

            // Exibe a notificação para o usuário
            showNotification(title, body);
        }
    }

    // Função responsável por criar e exibir a notificação no sistema
    private void showNotification(String title, String body) {
        String channelId = "timer_channel"; // ID do canal de notificações (obrigatório no Android 8.0+)

        // Intent que será aberta quando o usuário clicar na notificação (abre a MainActivity)
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Cria a notificação configurando título, texto, prioridade e ação ao clicar
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Ícone da notificação
                .setContentTitle(title) // Título da notificação
                .setContentText(body)   // Texto principal da notificação
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Alta prioridade (aparece no topo)
                .setContentIntent(pendingIntent) // Define ação ao clicar na notificação
                .setAutoCancel(true); // Fecha a notificação ao clicar

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Se a versão do Android 8.0 ou superior, é necessário criar um canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Canal do Timer", // Nome visível para o usuário nas configurações
                    NotificationManager.IMPORTANCE_HIGH // Importância alta para mostrar pop-ups
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Mostra a notificação para o usuário
        notificationManager.notify(1, builder.build());
    }

    // Método chamado quando o dispositivo gera um novo token FCM (geralmente na primeira vez ou quando muda)
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Pode ser usado para enviar o token para seu servidor se precisar gerenciar envios segmentados
        Log.d("FCM", "Novo token do dispositivo: " + token);
    }
}
