package com.example.mystopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private ImageButton btnReset, btnLap;
    private ListView lapListView;
    private ToggleButton btnStartPause;
    private boolean isRunning = false;
    private long startTime, elapsedTime;

    private final Handler handler = new Handler();
    private ArrayList<String> lapList;
    private ArrayAdapter<String> lapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnReset = findViewById(R.id.btnReset);
        btnLap = findViewById(R.id.btnLap);
        lapListView = findViewById(R.id.laplistView);

        lapList = new ArrayList<>();
        lapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapList);
        lapListView.setAdapter(lapAdapter);

        btnStartPause.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isRunning = true;
                startTime = SystemClock.elapsedRealtime() - elapsedTime;
                handler.postDelayed(updateTimer, 0);
            } else {
                isRunning = false;
                elapsedTime = SystemClock.elapsedRealtime() - startTime;
                handler.removeCallbacks(updateTimer);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                btnStartPause.setChecked(false);
                elapsedTime = 0;
                handler.removeCallbacks(updateTimer);
                updateUI();
                lapList.clear();
                lapAdapter.notifyDataSetChanged();
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    // Display lap time in a ListView or handle as needed
                    String lapTime = chronometer.getText().toString();
                    lapList.add("Lap " + lapList.size() + ": " + lapTime);
                    lapAdapter.notifyDataSetChanged();
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
