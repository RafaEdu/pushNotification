package com.application.pushNotification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private NumberPicker numberPickerHours;
    private NumberPicker numberPickerMinutes;
    private NumberPicker numberPickerSeconds;
    private Button btnStartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        numberPickerHours = findViewById(R.id.numberPickerHours);
        numberPickerMinutes = findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = findViewById(R.id.numberPickerSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);


        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);

        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);

        btnStartTimer.setOnClickListener(v -> {
            int hours = numberPickerHours.getValue();
            int minutes = numberPickerMinutes.getValue();
            int seconds = numberPickerSeconds.getValue();

            int totalSeconds = (hours * 3600) + (minutes * 60) + seconds;

            if (totalSeconds > 0) {
                Intent serviceIntent = new Intent(this, TimerService.class);
                serviceIntent.putExtra(TimerService.EXTRA_SECONDS, totalSeconds);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                Toast.makeText(this, "Cronômetro iniciado!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Por favor, defina um tempo válido!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
