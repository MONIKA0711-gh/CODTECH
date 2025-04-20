package com.weather;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp {

    public static void main(String[] args) {
        try {
            // API URL for New York (latitude, longitude)
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=40.71&longitude=-74.01&current_weather=true";

            // Create connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONObject currentWeather = json.getJSONObject("current_weather");

            // Display data
            System.out.println("📍 Current Weather in New York:");
            System.out.println("🌡 Temperature: " + currentWeather.getDouble("temperature") + " °C");
            System.out.println("💨 Wind Speed: " + currentWeather.getDouble("windspeed") + " km/h");
            System.out.println("🕒 Time: " + currentWeather.getString("time"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

