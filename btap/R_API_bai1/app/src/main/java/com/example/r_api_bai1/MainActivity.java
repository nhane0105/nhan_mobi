package com.example.r_api_bai1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnFetch;

    private CountryAdapter adapter;

    private ExecutorService executorService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnFetch = findViewById(R.id.btnFetch);

        adapter = new CountryAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCountries();
            }
        });
    }

    private void fetchCountries() {

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                ArrayList<Country> countries = NetworkUtils.fetchCountries();

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (countries.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Lỗi lấy dữ liệu!",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {
                            updateUI(countries);
                        }

                    }
                });

            }
        });
    }

    private void updateUI(ArrayList<Country> countries) {
        adapter.updateData(countries);
    }
}