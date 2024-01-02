package com.example.mystopwatch;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private TextView laptext;
    private ToggleButton toggleButton;
    private ImageButton btnlap, reset;

    private boolean isRunning = false;
    private long  startTime, elapsedTime;
    private int lapCount = 1;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        laptext = findViewById(R.id.laptext);
        toggleButton = findViewById(R.id.toggelbutton);
        reset = findViewById(R.id.reset);
        btnlap = findViewById(R.id.btnlap);

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Start the stopwatch
                isRunning = true;
                startTime = SystemClock.elapsedRealtime() - elapsedTime;
                handler.postDelayed(updateTimer, 0);
            } else {
                // Pause the stopwatch
                isRunning = false;
                elapsedTime = SystemClock.elapsedRealtime() - startTime;
                handler.removeCallbacks(updateTimer);
            }
        });

        reset.setOnClickListener(v -> {
            // Reset the stopwatch
            isRunning = false;
            toggleButton.setChecked(false);
            elapsedTime = 0;
            handler.removeCallbacks(updateTimer);
            updateUI();
        });

        btnlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRunning){
                    String lapTime = String.format("Lap %d: %s", lapCount, chronometer.getText());
                    lapCount++;
                    laptext.setText(String.valueOf(lapTime));
                }
            }
        });
    }

    private final Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                elapsedTime = SystemClock.elapsedRealtime() - startTime;
                updateUI();
                handler.postDelayed(this, 30); // Update every 30 milliseconds
            }
        }
    };

    private void updateUI() {
        int minutes = (int) (elapsedTime / 60000);
        int seconds = (int) (elapsedTime % 60000) / 1000;
        int milliseconds = (int) (elapsedTime % 1000);

        String timerText = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
        chronometer.setText(timerText);

    }
}