package com.example.application.places;



public class Weather {

    public String name,icon,main,desc,country;
    public int temp,humidity,pressure,wind_speed,wind_deg;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(int wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(int wind_deg) {
        this.wind_deg = wind_deg;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
            return country;
        }

    public void setCountry(String country) {
            this.country = country;
        }

    public int getTemp() {
            return temp;
        }

    public void setTemp(int temp) {
            this.temp = temp;
        }

    public int getHumidity() {
            return humidity;
        }

    public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

    public int getPressure() {
            return pressure;
        }

    public void setPressure(int pressure) {
            this.pressure = pressure;
        }

}
