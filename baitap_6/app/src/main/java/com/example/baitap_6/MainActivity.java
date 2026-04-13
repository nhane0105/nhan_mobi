package com.example.baitap_6;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtNumber;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNumber = findViewById(R.id.txtNumber);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> startCounting());
    }

    private void startCounting() {
        new Thread(() -> {

            for (int i = 1; i <= 10; i++) {

                int number = i;

                // cập nhật UI
                runOnUiThread(() -> txtNumber.setText(String.valueOf(number)));

                try {
                    Thread.sleep(1000); // delay 1 giây
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // xong
            runOnUiThread(() -> txtNumber.setText("Hoàn thành!"));

        }).start();
    }
}