package com.example.application.places;

import java.util.ArrayList;


public class Forecast {

    String city;
    String country;
    double lat,lon;
    ArrayList<Long> timeStamp;
    ArrayList<Double> tempDay,tempEvng,tempNight,tempMrng,tempMin,tempMax,pressure,windSpeed;
    ArrayList<Integer> humidity;
    ArrayList<String> main_weather, desc_weather;
    ArrayList<String> icon;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public ArrayList<Long> getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(ArrayList<Long> timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Double> getTempDay() {
        return tempDay;
    }

    public void setTempDay(ArrayList<Double> tempDay) {
        this.tempDay = tempDay;
    }

    public ArrayList<Double> getTempEvng() {
        return tempEvng;
    }

    public void setTempEvng(ArrayList<Double> tempEvng) {
        this.tempEvng = tempEvng;
    }

    public ArrayList<Double> getTempNight() {
        return tempNight;
    }

    public void setTempNight(ArrayList<Double> tempNight) {
        this.tempNight = tempNight;
    }

    public ArrayList<Double> getTempMrng() {
        return tempMrng;
    }

    public void setTempMrng(ArrayList<Double> tempMrng) {
        this.tempMrng = tempMrng;
    }

    public ArrayList<Double> getTempMin() {
        return tempMin;
    }

    public void setTempMin(ArrayList<Double> tempMin) {
        this.tempMin = tempMin;
    }

    public ArrayList<Double> getTempMax() {
        return tempMax;
    }

    public void setTempMax(ArrayList<Double> tempMax) {
        this.tempMax = tempMax;
    }

    public ArrayList<Double> getPressure() {
        return pressure;
    }

    public void setPressure(ArrayList<Double> pressure) {
        this.pressure = pressure;
    }

    public ArrayList<Double> getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(ArrayList<Double> windSpeed) {
        this.windSpeed = windSpeed;
    }

    public ArrayList<Integer> getHumidity() {
        return humidity;
    }

    public void setHumidity(ArrayList<Integer> humidity) {
        this.humidity = humidity;
    }

    public ArrayList<String> getMain_weather() {
        return main_weather;
    }

    public void setMain_weather(ArrayList<String> main_weather) {
        this.main_weather = main_weather;
    }

    public ArrayList<String> getDesc_weather() {
        return desc_weather;
    }

    public void setDesc_weather(ArrayList<String> desc_weather) {
        this.desc_weather = desc_weather;
    }

    public ArrayList<String> getIcon() {
        return icon;
    }

    public void setIcon(ArrayList<String> icon) {
        this.icon = icon;
    }
}
