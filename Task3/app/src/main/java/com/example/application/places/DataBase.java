package com.example.application.places;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataBase {

    private static String DATABASE_NAME = "database";
    private static int DATABASE_VERSION = 1;

    private static String TABLE_WEATHER = "weatherTable";
    private static String KEY_W_ID = "wId";
    private static String KEY_WNAME = "wName";
    private static String KEY_WCOUNTRY = "wCountry";
    private static String KEY_WMAIN = "wMain";
    private static String KEY_WDESC = "wDescription";
    private static String KEY_WTEMP = "wTemperature";
    private static String KEY_WHUMIDITY = "wHumidity";
    private static String KEY_WWINDSPEED = "wWindspeed";
    private static String KEY_WWINDDEG = "wWindDeg";
    private static String KEY_WPRESSURE = "wPressure";
    private static String KEY_WICON = "wIcon";

    private static String TABLE_FORECAST = "forecastTable";
    private static String KEY_F_ID = "fId";
    private static String KEY_FNAME = "fName";
    private static String KEY_FCOUNTRY = "fCountry";
    private static String KEY_FLON = "fLongitude";
    private static String KEY_FLAT = "fLatitude";
    private static String KEY_FDATE = "fDate";
    private static String KEY_FMAIN = "fMain";
    private static String KEY_FDESC = "fDescription";
    private static String KEY_FHUMIDITY = "fHumidity";
    private static String KEY_FPRESSURE = "fPressure";
    private static String KEY_FWINDSPEED = "fWindSpeed";
    private static String KEY_FTDAY = "fTday";
    private static String KEY_FTMORNING = "fTmorning";
    private static String KEY_FTEVENING = "fTevening";
    private static String KEY_FTNIGHT = "fTnight";
    private static String KEY_FTMAX = "fTmax";
    private static String KEY_FTMIN = "fTmin";
    private static String KEY_FICON = "fIcon";

    private static String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + " ( " + KEY_W_ID + " INTEGER PRIMARY KEY, "
            + KEY_WNAME + " TEXT, " + KEY_WCOUNTRY + " TEXT, " + KEY_WMAIN + " TEXT, " + KEY_WDESC + " TEXT, "
            + KEY_WTEMP + " INTEGER, " + KEY_WHUMIDITY + " INTEGER, " + KEY_WWINDSPEED + " INTEGER, "
            + KEY_WWINDDEG + " INTEGER, " + KEY_WPRESSURE + " INTEGER, " + KEY_WICON + " TEXT );";

    private static String CREATE_FORECAST_TABLE = "CREATE TABLE " + TABLE_FORECAST + " ( " + KEY_F_ID + " INTEGER PRIMARY KEY, "
            + KEY_FNAME + " TEXT, " + KEY_FCOUNTRY + " TEXT, " + KEY_FLON + " DOUBLE, " + KEY_FLAT + " DOUBLE, "
            + KEY_FDATE + " TEXT, " + KEY_FMAIN + " TEXT, " + KEY_FDESC + " TEXT, " + KEY_FHUMIDITY + " TEXT, "
            + KEY_FPRESSURE + " TEXT, " + KEY_FWINDSPEED + " TEXT, " + KEY_FTDAY + " TEXT, " + KEY_FTMORNING + " TEXT, "
            + KEY_FTEVENING + " TEXT, " + KEY_FTNIGHT + " TEXT, " + KEY_FTMAX + " TEXT, " + KEY_FTMIN + " TEXT, "
            + KEY_FICON + " TEXT );";

    Context context;
    DbHelper dbHelper;

    public DataBase(Context context) {
        this.context = context;
    }

    public class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_WEATHER_TABLE);
            db.execSQL(CREATE_FORECAST_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORECAST);
        }
    }

    public void createWeatherData(int position,Weather weather) {
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_W_ID,position);
        cv.put(KEY_WNAME, weather.getName());

        cv.put(KEY_WCOUNTRY, weather.getCountry());
        cv.put(KEY_WMAIN, weather.getMain());
        cv.put(KEY_WDESC, weather.getDesc());
        cv.put(KEY_WTEMP, weather.getTemp());
        cv.put(KEY_WHUMIDITY, weather.getHumidity());
        cv.put(KEY_WPRESSURE, weather.getPressure());
        cv.put(KEY_WWINDSPEED, weather.getWind_speed());
        cv.put(KEY_WWINDDEG, weather.getWind_deg());
        cv.put(KEY_WICON,weather.getIcon());
        Log.d("SAN","CREAting data : "+weather.getIcon());
        database.insert(TABLE_WEATHER, null, cv);
    }

    public Weather getWeatherData(String name){
        Weather weather = new Weather();
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] allColumns = {KEY_WCOUNTRY,KEY_WMAIN,KEY_WDESC,KEY_WTEMP,KEY_WHUMIDITY,KEY_WPRESSURE,
                KEY_WWINDSPEED,KEY_WWINDDEG,KEY_WICON};

        Cursor cursor = database.query(TABLE_WEATHER,allColumns,KEY_WNAME + " = ? ",new String[]{name},null,null,null);
        //int pName = cursor.getColumnIndex(KEY_WNAME);

        cursor.moveToFirst();
        int pCountry = cursor.getColumnIndex(KEY_WCOUNTRY);
        int pMain = cursor.getColumnIndex(KEY_WMAIN);
        int pDesc = cursor.getColumnIndex(KEY_WDESC);
        int pTemp = cursor.getColumnIndex(KEY_WTEMP);
        int pHumidity = cursor.getColumnIndex(KEY_WHUMIDITY);
        int pPressure = cursor.getColumnIndex(KEY_WPRESSURE);
        int pWindSpeed = cursor.getColumnIndex(KEY_WWINDSPEED);
        int pWindDeg = cursor.getColumnIndex(KEY_WWINDDEG);
        int pIcon = cursor.getColumnIndex(KEY_WICON);
        //weather.setName(cursor.getString(pName));
        weather.setName(name);
        weather.setCountry(cursor.getString(pCountry));
        weather.setMain(cursor.getString(pMain));
        weather.setDesc(cursor.getString(pDesc));
        weather.setTemp(cursor.getInt(pTemp));
        weather.setHumidity(cursor.getInt(pHumidity));
        weather.setPressure(cursor.getInt(pPressure));
        weather.setWind_speed(cursor.getInt(pWindSpeed));
        weather.setWind_deg(cursor.getInt(pWindDeg));
        weather.setIcon(cursor.getString(pIcon));
        return weather;
    }

    public ArrayList<String> getName(){
        ArrayList<String> names =new ArrayList<>();
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] column = {KEY_WNAME};
        Cursor cursor = database.query(TABLE_WEATHER,column,null,null,null,null,null);
        for(cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()){
            int pName = cursor.getColumnIndex(KEY_WNAME);
            String name = cursor.getString(pName);
            names.add(name);
        }
        return names;
    }


    public void createForecastData(int position,Forecast forecast){
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

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

        for(int i=0 ; i<forecast.getTimeStamp().size() ; i++){
            aTimeStamp.add(forecast.getTimeStamp().get(i));
            aTempDay.add(forecast.getTempDay().get(i));
            aTempEvng.add(forecast.getTempEvng().get(i));
            aTempNight.add(forecast.getTempNight().get(i));
            aTempMrng.add(forecast.getTempMrng().get(i));
            aTempMin.add(forecast.getTempMin().get(i));
            aTempMax.add(forecast.getTempMax().get(i));
            aPressure.add(forecast.getPressure().get(i));
            aWindSpeed.add(forecast.getWindSpeed().get(i));
            aHumidity.add(forecast.getHumidity().get(i));
            aMain_weather.add(forecast.getMain_weather().get(i));
            aDesc_weather.add(forecast.getDesc_weather().get(i));
            aIcon.add(forecast.getIcon().get(i));
        }

        ContentValues cv = new ContentValues();
        cv.put(KEY_F_ID,position);
        cv.put(KEY_FNAME,forecast.getCity());
        cv.put(KEY_FCOUNTRY,forecast.getCountry());
        cv.put(KEY_FLON,forecast.getLon());
        cv.put(KEY_FLAT,forecast.getLat());

        Gson gson = new Gson();
        String timeStamp = gson.toJson(aTimeStamp);
        String main_weather = gson.toJson(aMain_weather);
        String desc_weather = gson.toJson(aDesc_weather);
        String humidity = gson.toJson(aHumidity);
        String pressure = gson.toJson(aPressure);
        String windSpeed = gson.toJson(aWindSpeed);
        String tempDay = gson.toJson(aTempDay);
        String tempMrng = gson.toJson(aTempMrng);
        String tempEvng = gson.toJson(aTempEvng);
        String tempNight = gson.toJson(aTempNight);
        String tempMax = gson.toJson(aTempMax);
        String tempMin = gson.toJson(aTempMin);
        String icon = gson.toJson(aIcon);

        cv.put(KEY_FDATE,timeStamp);
        cv.put(KEY_FMAIN,main_weather);
        cv.put(KEY_FDESC,desc_weather);
        cv.put(KEY_FHUMIDITY,humidity);
        cv.put(KEY_FPRESSURE,pressure);
        cv.put(KEY_FWINDSPEED,windSpeed);
        cv.put(KEY_FTDAY,tempDay);
        cv.put(KEY_FTMORNING,tempMrng);
        cv.put(KEY_FTEVENING,tempEvng);
        cv.put(KEY_FTNIGHT,tempNight);
        cv.put(KEY_FTMAX,tempMax);
        cv.put(KEY_FTMIN,tempMin);
        cv.put(KEY_FICON,icon);
        database.insert(TABLE_FORECAST,null,cv);
    }



    public Forecast getForecastData(String name){
        Forecast forecast = new Forecast();
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] allColumns = {KEY_FCOUNTRY,KEY_FLON,KEY_FLAT,KEY_FDATE,KEY_FMAIN,KEY_FDESC,KEY_FHUMIDITY,KEY_FPRESSURE,
                KEY_FWINDSPEED,KEY_FTDAY,KEY_FTMORNING,KEY_FTEVENING,KEY_FTNIGHT,KEY_FTMAX,KEY_FTMIN,KEY_FICON};
        Cursor cursor = database.query(TABLE_FORECAST,allColumns,KEY_FNAME + " = ? ",new String[]{name},null,null,null);
        cursor.moveToFirst();

        int pCountry = cursor.getColumnIndex(KEY_FCOUNTRY);
        int pLon = cursor.getColumnIndex(KEY_FLON);
        int pLat = cursor.getColumnIndex(KEY_FLAT);
        int pDate = cursor.getColumnIndex(KEY_FDATE);
        int pMain = cursor.getColumnIndex(KEY_FMAIN);
        int pDesc = cursor.getColumnIndex(KEY_FDESC);
        int pHumidity = cursor.getColumnIndex(KEY_FHUMIDITY);
        int pPressure = cursor.getColumnIndex(KEY_FPRESSURE);
        int pWindSpeed = cursor.getColumnIndex(KEY_FWINDSPEED);
        int pTDay = cursor.getColumnIndex(KEY_FTDAY);
        int pTMorning = cursor.getColumnIndex(KEY_FTMORNING);
        int pTEvening = cursor.getColumnIndex(KEY_FTEVENING);
        int pTNight = cursor.getColumnIndex(KEY_FTNIGHT);
        int pTMax = cursor.getColumnIndex(KEY_FTMAX);
        int pTMin = cursor.getColumnIndex(KEY_FTMIN);
        int pIcon = cursor.getColumnIndex(KEY_FICON);

        forecast.setCity(name);
        forecast.setCountry(cursor.getString(pCountry));
        forecast.setLon(cursor.getDouble(pLon));
        forecast.setLat(cursor.getDouble(pLat));

        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        Gson gson = new Gson();
        ArrayList<Long>  aDate = gson.fromJson(cursor.getString(pDate), type);
        forecast.setTimeStamp(aDate);
        ArrayList<String>  aMain = gson.fromJson(cursor.getString(pMain), type);
        forecast.setMain_weather(aMain);
        ArrayList<String>  aDesc = gson.fromJson(cursor.getString(pDesc), type);
        forecast.setDesc_weather(aDesc);
        ArrayList<Integer>  aHumidity = gson.fromJson(cursor.getString(pHumidity), type);
        forecast.setHumidity(aHumidity);
        ArrayList<Double>  aPressure = gson.fromJson(cursor.getString(pPressure), type);
        forecast.setPressure(aPressure);
        ArrayList<Double>  aWindSpeed = gson.fromJson(cursor.getString(pWindSpeed), type);
        forecast.setWindSpeed(aWindSpeed);
        ArrayList<Double>  aTDay = gson.fromJson(cursor.getString(pTDay), type);
        forecast.setTempDay(aTDay);
        ArrayList<Double>  aTMorning = gson.fromJson(cursor.getString(pTMorning), type);
        forecast.setTempMrng(aTMorning);
        ArrayList<Double>  aTEvening = gson.fromJson(cursor.getString(pTEvening), type);
        forecast.setTempEvng(aTEvening);
        ArrayList<Double>  aTNight = gson.fromJson(cursor.getString(pTNight), type);
        forecast.setTempNight(aTNight);
        ArrayList<Double>  aTMax = gson.fromJson(cursor.getString(pTMax), type);
        forecast.setTempMax(aTMax);
        ArrayList<Double>  aTMin = gson.fromJson(cursor.getString(pTMin), type);
        forecast.setTempMin(aTMin);
        ArrayList<String>  aIcon = gson.fromJson(cursor.getString(pIcon), type);
        forecast.setIcon(aIcon);

        return forecast;
    }
}


