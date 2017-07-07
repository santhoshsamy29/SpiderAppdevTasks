package com.example.application.places;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    DataBase dataBase = new DataBase(this);

    CardView cardView;
    TextView name,main,desc,temp,humidity,pressure,wind_speed,wind_deg;
    TextView forecast_text;
    AutoCompleteTextView search_et;
    Button search_button;
    ImageView imgView;
    ProgressBar progressBar;
    PlaceAutocompleteFragment autocompleteFragment;
    JSONParser jsonParser = new JSONParser();
    HttpClient httpClient = new HttpClient();

    String location;
    String offline_location;
    public static int POSITION = 0;
    String[] names_arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("STATE",Context.MODE_PRIVATE);
        POSITION = preferences.getInt("POSITION",0);
        Log.e("SAN","POSITION : " + POSITION);

        cardView = (CardView)findViewById(R.id.cv);
        name = (TextView)findViewById(R.id.wName);
        main = (TextView)findViewById(R.id.wMain);
        desc = (TextView)findViewById(R.id.wDesc);
        temp = (TextView)findViewById(R.id.wTemp);
        humidity = (TextView)findViewById(R.id.wHumidity);
        pressure = (TextView)findViewById(R.id.wPressure);
        wind_speed = (TextView)findViewById(R.id.wWind_speed);
        wind_deg = (TextView)findViewById(R.id.wWind_deg);
        progressBar = (ProgressBar)findViewById(R.id.wProgress);

        search_button = (Button)findViewById(R.id.search_button);
        search_et = (AutoCompleteTextView) findViewById(R.id.search_et);

        ArrayList<String> names = dataBase.getName();

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(names);
        names.clear();
        names.addAll(hashSet);
        names_arr = new String[hashSet.size()];
        names_arr = hashSet.toArray(names_arr);

        ArrayAdapter text_adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,names_arr);
        search_et.setAdapter(text_adapter);

        imgView= (ImageView)findViewById(R.id.wImage);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);

        if(isNetworkAvailable()){
            autocompleteFragment.getView().setVisibility(View.VISIBLE);
        }
        else{
            autocompleteFragment.getView().setVisibility(View.INVISIBLE);
            search_et.setVisibility(View.VISIBLE);
            search_button.setVisibility(View.VISIBLE);
        }

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offline_location = search_et.getText().toString();
                InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(search_et.getWindowToken(),0);
                search_et.setText("");
                if(isNameAvailable(offline_location)){
                    cardView.setVisibility(View.VISIBLE);
                    forecast_text.setVisibility(View.VISIBLE);
                    Weather temp_weather = dataBase.getWeatherData(offline_location);
                    setValues(temp_weather);
                }else {
                    Toast.makeText(MainActivity.this,"No such city available in Database",Toast.LENGTH_SHORT).show();
                }

            }
        });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e("SAN", "Place selected: " + place.getName());
                location = place.getName().toString();
                WeatherTask task = new WeatherTask();
                task.execute(location);

            }

            @Override
            public void onError(Status status) {
                Log.e("SAN", "Place selected: ERROR ");
            }
        });
        autocompleteFragment.setHint("Search a Location");

        forecast_text = (TextView)findViewById(R.id.forecast_text);
        forecast_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fIntent = new Intent(MainActivity.this,ForecastActivity.class);
                fIntent.putExtra("Location",location);
                fIntent.putExtra("OFF_Location",offline_location);
                startActivity(fIntent);
            }
        });
    }

    public boolean isNameAvailable(String location){
        for (int i=0 ; i<names_arr.length ; i++){
            if(location.equalsIgnoreCase(names_arr[i])){
                return true;
            }
        }
        return false;
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();

            String data = httpClient.getWeather(params[0]);

            Log.e("SAN","Data recieved");
            try {
                weather = jsonParser.getWeatherData(data);
                Log.e("SAN","Parser started ");

                //weather.img = httpClient.getImage(weather.getIcon());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            progressBar.setVisibility(View.INVISIBLE);
            forecast_text.setVisibility(View.VISIBLE);

            dataBase.createWeatherData(POSITION,weather);
            POSITION++;

            /*
            name.setText( weather.getName() + "," + weather.getCountry() );
            main.setText(weather.getMain() + " :");
            desc.setText(weather.getDesc());
            temp.setText(    "Temperature : " + (weather.getTemp()-273) + (char) 0x00B0 + "C");
            humidity.setText(  "Humidity       : " + weather.getHumidity() + "%");
            pressure.setText(  "Pressure       : " + weather.getPressure() + "hPa");
            wind_speed.setText("Wind Speed  : " + weather.getWind_speed() + "m/s");
            wind_deg.setText("Wind degree : " + weather.getWind_deg()+ (char) 0x00B0);

            URL img_url = null;
            try {
                 img_url = new URL("http://openweathermap.org/img/w/" + weather.getIcon() + ".png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Glide.with(getApplicationContext())
                    .load(img_url)
                    .into(imgView);
                    */

            setValues(weather);

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setValues(Weather weather){
        name.setText( weather.getName() + "," + weather.getCountry() );
        main.setText(weather.getMain() + " :");
        desc.setText(weather.getDesc());
        temp.setText(    "Temperature : " + (weather.getTemp()-273) + (char) 0x00B0 + "C");
        humidity.setText(  "Humidity       : " + weather.getHumidity() + "%");
        pressure.setText(  "Pressure       : " + weather.getPressure() + "hPa");
        wind_speed.setText("Wind Speed  : " + weather.getWind_speed() + "m/s");
        wind_deg.setText("Wind degree : " + weather.getWind_deg()+ (char) 0x00B0);

        URL img_url = null;
        try {
            img_url = new URL("http://openweathermap.org/img/w/" + weather.getIcon() + ".png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Glide.with(getApplicationContext())
                .load(img_url)
                .into(imgView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("STATE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("POSITION",POSITION);
        editor.commit();
    }
}
