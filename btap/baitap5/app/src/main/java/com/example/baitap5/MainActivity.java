package com.example.baitap5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btnTest;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.btnTest);
        txtResult = findViewById(R.id.txtResult);

        btnTest.setOnClickListener(v -> testSpeed());
    }

    private void testSpeed() {

        txtResult.setText("Đang đo...");

        new Thread(() -> {

            try {
                OkHttpClient client = new OkHttpClient();

                long startTime = System.currentTimeMillis();

                Request request = new Request.Builder()
                        .url("https://www.google.com")
                        .build();

                Response response = client.newCall(request).execute();

                long endTime = System.currentTimeMillis();

                long speed = endTime - startTime;

                runOnUiThread(() ->
                        txtResult.setText("Thời gian phản hồi: " + speed + " ms")
                );

            } catch (IOException e) {
                runOnUiThread(() ->
                        txtResult.setText("Lỗi kết nối!")
                );
            }

        }).start();
    }
}
