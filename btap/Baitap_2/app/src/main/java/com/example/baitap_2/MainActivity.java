package com.example.baitap_2;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button startButton, stopButton;
    private ProgressBar progressBar;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Thread thread;
    private volatile boolean isRunning = false; // cờ để stop thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        progressBar = findViewById(R.id.progressBar);

        startButton.setOnClickListener(v -> startCounting());
        stopButton.setOnClickListener(v -> stopCounting());
    }

    private void startCounting() {
        // tránh bấm Start nhiều lần chạy nhiều thread
        if (isRunning) return;

        isRunning = true;

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        textView.setText("0");

        thread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {

                if (!isRunning) return;

                int count = i;

                handler.post(() -> {
                    textView.setText(String.valueOf(count));
                    progressBar.setProgress(count);
                });

                try {
                    Thread.sleep(1000); // 1 giây
                } catch (InterruptedException e) {
                    return;
                }
            }

            // xong thì hiển thị hoàn thành
            handler.post(() -> {
                textView.setText("Hoàn thành!");
                progressBar.setVisibility(View.GONE);
                isRunning = false;
            });
        });

        thread.start();
    }

    private void stopCounting() {
        isRunning = false;

        if (thread != null) {
            thread.interrupt();
            thread = null;
        }

        handler.post(() -> {
            textView.setText("Đã dừng!");
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(0);
        });
    }
}