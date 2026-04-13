package com.example.r_api_baitap2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtEmail;
    Button btnPost, btnGet;
    TextView txtResult;

    String API = "https://jsonplaceholder.typicode.com/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnPost = findViewById(R.id.btnPost);
        btnGet = findViewById(R.id.btnGet);
        txtResult = findViewById(R.id.txtResult);

        btnGet.setOnClickListener(v -> new GetUsersTask().execute(API));

        btnPost.setOnClickListener(v -> {

            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();

            new PostUserTask().execute(name, email);
        });
    }

    class GetUsersTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                reader.close();

            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }

            return result.toString();
        }

        protected void onPostExecute(String s) {
            txtResult.setText(s);
        }
    }

    class PostUserTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            try {

                URL url = new URL(API);

                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("name", params[0]);
                json.put("email", params[1]);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    response.append(line);
                }

                reader.close();

                return response.toString();

            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        protected void onPostExecute(String s) {

            try {
                JSONObject json = new JSONObject(s);
                String name = json.optString("name");
                String email = json.optString("email");
                String id = json.optString("id");

                StringBuilder builder = new StringBuilder();
                builder.append("User mới tạo:\n");
                builder.append("Tên: ").append(name).append("\n");
                builder.append("Email: ").append(email);
                if (!id.isEmpty()) {
                    builder.append("\nId: ").append(id);
                }

                txtResult.setText(builder.toString());
            } catch (Exception e) {
                txtResult.setText("POST response:\n" + s);
            }
        }
    }
}
