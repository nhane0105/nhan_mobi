package com.example.baitap_1;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        if (isNetworkAvailable()) {
            new CheckInternetTask(executorService, mainHandler, statusText).execute("https://www.google.com");
        } else {
            statusText.setText(getString(R.string.no_network));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        Network network = cm.getActiveNetwork();
        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                 capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                 capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private static class CheckInternetTask {
        private final ExecutorService executor;
        private final Handler mainHandler;
        private final TextView statusText;

        CheckInternetTask(ExecutorService executor, Handler mainHandler, TextView statusText) {
            this.executor = executor;
            this.mainHandler = mainHandler;
            this.statusText = statusText;
        }

        void execute(String url) {
            executor.execute(() -> {
                boolean result = checkConnection(url);
                mainHandler.post(() -> {
                    if (result) {
                        statusText.setText(statusText.getContext().getString(R.string.connected_success));
                    } else {
                        statusText.setText(statusText.getContext().getString(R.string.cannot_connect));
                    }
                });
            });
        }

        private boolean checkConnection(String urlString) {
            try {
                HttpURLConnection urlConnection =
                        (HttpURLConnection) new URL(urlString).openConnection();

                urlConnection.setRequestMethod("HEAD");
                urlConnection.setConnectTimeout(3000);
                urlConnection.setReadTimeout(3000);
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                return (responseCode == 200);

            } catch (IOException e) {
                return false;
            }
        }
    }
}