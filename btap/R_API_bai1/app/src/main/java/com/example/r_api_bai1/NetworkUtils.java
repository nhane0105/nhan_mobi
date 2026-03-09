package com.example.r_api_bai1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    private static final String API_URL = "https://restcountries.com/v3.1/all?fields=name,capital,region,flags";

    public static ArrayList<Country> fetchCountries() {
        ArrayList<Country> countries = new ArrayList<>();

        HttpURLConnection conn = null;
        try {
            URL url = new URL(API_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("User-Agent", "R_API_Bai1/1.0");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject countryJson = jsonArray.getJSONObject(i);
                        JSONObject nameObj = countryJson.optJSONObject("name");
                        if (nameObj == null) continue;

                        String name = nameObj.optString("common", "Unknown");

                        String capital = "N/A";
                        if (countryJson.has("capital")) {
                            JSONArray capitalArr = countryJson.getJSONArray("capital");
                            if (capitalArr.length() > 0) {
                                capital = capitalArr.optString(0, "N/A");
                            }
                        }

                        String region = countryJson.optString("region", "N/A");

                        String flagUrl = "";
                        if (countryJson.has("flags")) {
                            JSONObject flagsObj = countryJson.getJSONObject("flags");
                            flagUrl = flagsObj.optString("png", flagsObj.optString("svg", ""));
                        }

                        countries.add(new Country(name, capital, region, flagUrl));
                    } catch (Exception e) {
                        Log.w(TAG, "Skip country at index " + i + ": " + e.getMessage());
                    }
                }
            } else {
                Log.e(TAG, "HTTP error: " + responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return countries;
    }
}