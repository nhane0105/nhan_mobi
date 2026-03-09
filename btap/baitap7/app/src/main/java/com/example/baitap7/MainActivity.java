package com.example.baitap7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button btnPing;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPing = findViewById(R.id.btnPing);
        txtResult = findViewById(R.id.txtResult);

        btnPing.setOnClickListener(v -> startPing());
    }

    private void startPing() {

        txtResult.setText("Đang ping...");

        new Thread(() -> {

            StringBuilder result = new StringBuilder();

            try {

                Process process = Runtime.getRuntime()
                        .exec("ping -c 4 google.com");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                runOnUiThread(() ->
                        txtResult.setText(result.toString())
                );

            } catch (Exception e) {
                runOnUiThread(() ->
                        txtResult.setText("Ping thất bại!")
                );
            }

        }).start();
    }
}
