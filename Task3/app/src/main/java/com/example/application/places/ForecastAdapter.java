package com.example.application.places;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyHolder> {

    Context context;
    Forecast forecast;

    public ForecastAdapter(Context context, Forecast forecast) {
        this.context = context;
        this.forecast = forecast;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_forecast,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.main.setText(forecast.getMain_weather().get(position+1) + " : " + forecast.getDesc_weather().get(position+1));
        holder.humidity.setText("Humidity : " + forecast.getHumidity().get(position+1) + "%");
        holder.pressure.setText("Pressure : " + forecast.getPressure().get(position+1) + "hpa");
        holder.windSpeed.setText("Wind Speed : " + forecast.getWindSpeed().get(position+1) + "m/s");
        holder.tempDay.setText("Day : " + forecast.getTempDay().get(position+1)+ (char) 0x00B0 + "C");
        holder.tempMrng.setText("Morning : " + forecast.getTempMrng().get(position+1)+ (char) 0x00B0 + "C");
        holder.tempEvng.setText("Evening : " + forecast.getTempEvng().get(position+1)+ (char) 0x00B0 + "C");
        holder.tempNight.setText("Night : " + forecast.getTempNight().get(position+1)+ (char) 0x00B0 + "C");
        holder.tempMax.setText("Maximum : " + forecast.getTempMax().get(position+1)+ (char) 0x00B0 + "C");
        holder.tempMin.setText("Minimum : " + forecast.getTempMin().get(position+1)+ (char) 0x00B0 + "C");
        URL img_url = null;
        try {
            img_url = new URL("http://openweathermap.org/img/w/" + forecast.getIcon().get(position) + ".png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Glide.with(context)
                .load(img_url)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return (forecast.timeStamp.size()-1);
    }


    public class MyHolder extends RecyclerView.ViewHolder{

        TextView main,humidity,pressure,windSpeed,tempDay,tempMrng,tempEvng,tempNight,tempMax,tempMin;
        ImageView img;

        public MyHolder(View itemView) {
            super(itemView);
            main = (TextView)itemView.findViewById(R.id.fMainWeather);
            humidity = (TextView)itemView.findViewById(R.id.fHumidity);
            pressure = (TextView)itemView.findViewById(R.id.fPressure);
            windSpeed = (TextView)itemView.findViewById(R.id.fWindSpeed);
            tempDay = (TextView)itemView.findViewById(R.id.fTempDay);
            tempMrng = (TextView)itemView.findViewById(R.id.fTempMrng);
            tempEvng = (TextView)itemView.findViewById(R.id.fTempEvng);
            tempNight = (TextView)itemView.findViewById(R.id.fTempNight);
            tempMax = (TextView)itemView.findViewById(R.id.fTempMax);
            tempMin = (TextView)itemView.findViewById(R.id.fTempMin);
            img = (ImageView)itemView.findViewById(R.id.fImg);
        }
    }
}
