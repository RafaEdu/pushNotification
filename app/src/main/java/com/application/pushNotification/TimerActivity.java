package com.application.pushNotification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    // Componentes da interface
    private NumberPicker numberPickerHours;
    private NumberPicker numberPickerMinutes;
    private NumberPicker numberPickerSeconds;
    private Button btnStartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer); // Define qual layout será exibido

        // Ligando os elementos do XML às variáveis Java
        numberPickerHours = findViewById(R.id.numberPickerHours);
        numberPickerMinutes = findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = findViewById(R.id.numberPickerSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);

        // Configura os intervalos possíveis para cada NumberPicker
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);

        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);

        // Ação ao clicar no botão de iniciar o cronômetro
        btnStartTimer.setOnClickListener(v -> {
            // Captura os valores selecionados pelo usuário
            int hours = numberPickerHours.getValue();
            int minutes = numberPickerMinutes.getValue();
            int seconds = numberPickerSeconds.getValue();

            // Converte tudo para segundos para facilitar o controle do tempo
            int totalSeconds = (hours * 3600) + (minutes * 60) + seconds;

            // Verifica se foi configurado um tempo válido
            if (totalSeconds > 0) {
                // Cria um Intent para iniciar o TimerService
                Intent serviceIntent = new Intent(this, TimerService.class);
                serviceIntent.putExtra(TimerService.EXTRA_SECONDS, totalSeconds); // Envia os segundos para o serviço

                // Verifica a versão do Android para iniciar o serviço corretamente
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent); // Android 8.0 ou superior exige foreground service
                } else {
                    startService(serviceIntent); // Em versões anteriores, serviço normal
                }

                // Informa ao usuário que o cronômetro foi iniciado
                Toast.makeText(this, "Cronômetro iniciado!", Toast.LENGTH_SHORT).show();
            } else {
                // Exibe uma mensagem caso o tempo seja inválido (0 segundos)
                Toast.makeText(this, "Por favor, defina um tempo válido!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
