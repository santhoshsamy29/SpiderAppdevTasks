package com.example.application.places;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser {
    public Weather getWeatherData(String data)throws JSONException{

        Weather weather = new Weather();

        JSONObject jObject = new JSONObject(data);

        weather.setName(getString("name",jObject));

        JSONObject sysObject = getObject("sys",jObject);
        weather.setCountry(getString("country",sysObject));

        JSONObject windObject = getObject("wind",jObject);
        weather.setWind_speed(getInt("speed",windObject));
        weather.setWind_deg(getInt("deg",windObject));

        JSONObject mainObject = getObject("main",jObject);
        weather.setTemp(getInt("temp",mainObject));
        weather.setHumidity(getInt("humidity",mainObject));
        weather.setPressure(getInt("pressure",mainObject));

        JSONArray weatherArray = jObject.getJSONArray("weather");

        JSONObject weatherObject = weatherArray.getJSONObject(0);
        weather.setIcon(getString("icon",weatherObject));
        weather.setMain(getString("main",weatherObject));
        weather.setDesc(getString("description",weatherObject));

        return weather;
    }

    public Forecast getForecastData(String data) throws JSONException{

        ArrayList<Long> aTimeStamp = new ArrayList<>();
        ArrayList<Double> aTempDay = new ArrayList<>();
        ArrayList<Double> aTempEvng = new ArrayList<>();
        ArrayList<Double> aTempNight = new ArrayList<>();
        ArrayList<Double> aTempMrng = new ArrayList<>();
        ArrayList<Double> aTempMin = new ArrayList<>();
        ArrayList<Double> aTempMax = new ArrayList<>();
        ArrayList<Double> aPressure = new ArrayList<>();
        ArrayList<Double> aWindSpeed = new ArrayList<>();

        ArrayList<Integer> aHumidity = new ArrayList<>();

        ArrayList<String> aMain_weather = new ArrayList<>();
        ArrayList<String> aDesc_weather = new ArrayList<>();
        ArrayList<String> aIcon = new ArrayList<>();

        Forecast forecast = new Forecast();
        JSONObject jObj = new JSONObject(data);

        JSONObject cityObj = jObj.getJSONObject("city");
        forecast.setCity(cityObj.getString("name"));
        forecast.setCountry(cityObj.getString("country"));
        JSONObject coordObj = cityObj.getJSONObject("coord");
        forecast.setLon(coordObj.getDouble("lon"));
        forecast.setLat(coordObj.getDouble("lat"));

        JSONArray listArray = jObj.getJSONArray("list");
        for(int i=0 ; i<listArray.length() ; i++){

            JSONObject listObj = listArray.getJSONObject(i);
            long timeStamp = listObj.getLong("dt");
            double pressure = listObj.getDouble("pressure");
            int humidity = listObj.getInt("humidity");
            double windSpeed = listObj.getDouble("speed");

            aTimeStamp.add(timeStamp);
            aPressure.add(pressure);
            aHumidity.add(humidity);
            aWindSpeed.add(windSpeed);

            JSONObject tempObj = listObj.getJSONObject("temp");
            double tempDay = tempObj.getDouble("day");
            double tempMin = tempObj.getDouble("min");
            double tempMax = tempObj.getDouble("max");
            double tempNight = tempObj.getDouble("night");
            double tempEvng = tempObj.getDouble("eve");
            double tempMrng = tempObj.getDouble("morn");

            aTempDay.add(tempDay);
            aTempMin.add(tempMin);
            aTempMax.add(tempMax);
            aTempNight.add(tempNight);
            aTempEvng.add(tempEvng);
            aTempMrng.add(tempMrng);

            JSONArray weatherArray = listObj.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            String main_weather = weatherObj.getString("main");
            String desc_weather = weatherObj.getString("description");
            String icon = weatherObj.getString("icon");

            aMain_weather.add(main_weather);
            aDesc_weather.add(desc_weather);
            aIcon.add(icon);
        }
        forecast.setTimeStamp(aTimeStamp);
        forecast.setTempDay(aTempDay);
        forecast.setTempEvng(aTempEvng);
        forecast.setTempMax(aTempMax);
        forecast.setTempMin(aTempMin);
        forecast.setTempMrng(aTempMrng);
        forecast.setTempNight(aTempNight);
        forecast.setPressure(aPressure);
        forecast.setHumidity(aHumidity);
        forecast.setWindSpeed(aWindSpeed);
        forecast.setMain_weather(aMain_weather);
        forecast.setDesc_weather(aDesc_weather);
        forecast.setIcon(aIcon);

        return forecast;
    }

    private static JSONObject getObject(String tag, JSONObject jObject) throws JSONException {
        JSONObject subObj = jObject.getJSONObject(tag);
        return subObj;
    }

    private static String getString(String tag, JSONObject jObject) throws JSONException {
        return jObject.getString(tag);
    }

    private static int getInt(String tag, JSONObject jObject) throws JSONException {
        return jObject.getInt(tag);
    }
}
