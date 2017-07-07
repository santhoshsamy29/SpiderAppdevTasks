package com.example.application.places;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ForecastActivity extends AppCompatActivity {

    HttpClient httpClient = new HttpClient();
    JSONParser jsonParser = new JSONParser();

    DataBase dataBase = new DataBase(this);
    ForecastAdapter forecastAdapter;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView name,lon,lat;
    public static int F_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        SharedPreferences preferences = getSharedPreferences("F_STATE", Context.MODE_PRIVATE);
        F_POSITION = preferences.getInt("FPOSITION",0);

        progressBar = (ProgressBar)findViewById(R.id.fProgress);
        name = (TextView)findViewById(R.id.fName);
        lon = (TextView)findViewById(R.id.fLon);
        lat = (TextView)findViewById(R.id.fLat);

        Bundle bundle = getIntent().getExtras();

        recyclerView = (RecyclerView)findViewById(R.id.fRv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(isNetworkAvailable()){
            String loc = bundle.getString("Location").toString();
            ForecastTask forecastTask = new ForecastTask();
            forecastTask.execute(loc);
        }else {
            String off_loc = bundle.getString("OFF_Location").toString();
            Forecast forecast = new Forecast();
            forecast = dataBase.getForecastData(off_loc);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            name.setText(forecast.getCity() + "," + forecast.getCountry());
            lon.setText("Longitude : " + forecast.getLon());
            lat.setText("Latitude : " + forecast.getLat());

            forecastAdapter = new ForecastAdapter(ForecastActivity.this,forecast);
            recyclerView.setAdapter(forecastAdapter);
        }
    }

    public class ForecastTask extends AsyncTask<String,Void,Forecast>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Forecast doInBackground(String... params) {
            Forecast forecast = new Forecast();

            String data = httpClient.getForecast(params[0]);
            if(data == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ForecastActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
            try {
                forecast = jsonParser.getForecastData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;
        }

        @Override
        protected void onPostExecute(Forecast forecast) {
            super.onPostExecute(forecast);

            dataBase.createForecastData(F_POSITION,forecast);
            F_POSITION++;
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            name.setText(forecast.getCity() + "," + forecast.getCountry());
            lon.setText("Longitude : " + forecast.getLon());
            lat.setText("Latitude : " + forecast.getLat());

            forecastAdapter = new ForecastAdapter(ForecastActivity.this,forecast);
            recyclerView.setAdapter(forecastAdapter);

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("F_STATE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("FPOSITION",F_POSITION);
        editor.commit();
    }
}
