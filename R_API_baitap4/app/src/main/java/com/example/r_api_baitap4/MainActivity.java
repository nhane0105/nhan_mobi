package com.example.r_api_baitap4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText edtCity;
    TextView txtWeather;
    Button btnGetWeather;

    private static final String API_KEY = "99b96d6e4ea73e5b98b06fa352ed3c92";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCity = findViewById(R.id.edtCity);
        txtWeather = findViewById(R.id.txtWeather);
        btnGetWeather = findViewById(R.id.btnGetWeather);

        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String city = edtCity.getText().toString();
                new GetWeatherTask().execute(city);

            }
        });
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String city = params[0].trim();
            if (city.isEmpty()) {
                return "Lỗi: Bạn chưa nhập tên thành phố";
            }

            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + API_KEY + "&units=metric";

            StringBuilder result = new StringBuilder();

            try {

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                BufferedReader reader;
                if (responseCode >= 200 && responseCode < 300) {
                    reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                } else {
                    reader = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream())
                    );
                }

                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                connection.disconnect();

            } catch (Exception e) {
                return "Lỗi: " + e.getMessage();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            // Nếu là chuỗi lỗi do doInBackground trả về
            if (s.startsWith("Lỗi:")) {
                txtWeather.setText(s);
                return;
            }

            try {

                JSONObject jsonObject = new JSONObject(s);

                // Nếu API trả về lỗi (ví dụ city not found, invalid API key...)
                if (jsonObject.has("cod") && !jsonObject.getString("cod").equals("200")) {
                    String message = jsonObject.optString("message", "Không lấy được dữ liệu từ server");
                    txtWeather.setText("Lỗi API: " + message);
                    return;
                }

                String cityName = jsonObject.getString("name");

                JSONObject main = jsonObject.getJSONObject("main");

                double temp = main.getDouble("temp");

                txtWeather.setText(
                        "Thành phố: " + cityName +
                                "\nNhiệt độ: " + temp + "°C"
                );

            } catch (Exception e) {

                txtWeather.setText("Không lấy được dữ liệu: " + e.getMessage());

            }

        }
    }
}