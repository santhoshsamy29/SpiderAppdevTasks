package com.example.application.places;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {

    public static String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String FORECAST_URL_START="http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private String FORECAST_URL_END = "&mode=json&units=metric&cnt=6&APPID=9ea04d05517dd3d56a1c1fa59230149e";
    private static String apiKey = "&APPID=9ea04d05517dd3d56a1c1fa59230149e";

    public String getWeather(String place){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            conn = (HttpURLConnection) (new URL(WEATHER_URL + place + apiKey)).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            StringBuffer myBuffer = new StringBuffer();
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while((line = br.readLine()) != null){
                myBuffer.append(line + "\n");
            }

            is.close();
            conn.disconnect();
            return myBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getForecast(String place){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            conn = (HttpURLConnection) (new URL(FORECAST_URL_START + place + FORECAST_URL_END)).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            StringBuffer buffer = new StringBuffer();
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = br.readLine()) != null){
                buffer.append(line + "/n");
            }
            is.close();
            conn.disconnect();
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
