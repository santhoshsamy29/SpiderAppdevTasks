package com.example.application.spidertask2;


import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    ImageView near,far;
    SensorManager proxyManager;
    Sensor proxy;
    SensorEventListener proxyListener;
    MediaPlayer mediaPlayer;
    boolean hidden= false;
    int x;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        state = 0;
        mediaPlayer = MediaPlayer.create(this,R.raw.alarm);
        mediaPlayer.setLooping(true);

        tv= (TextView) findViewById(R.id.tv);
        near = (ImageView) findViewById(R.id.near);
        far = (ImageView) findViewById(R.id.far);
        proxyManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        proxy = proxyManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        proxyListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                final MyTask myTask = new MyTask();
                if(event.values[0] < proxy.getMaximumRange()){
                    near.setImageResource(R.drawable.near);
                    near.setVisibility(View.VISIBLE);
                    far.setVisibility(View.INVISIBLE);
                    hidden = true;
                    x=10;
                    final Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(hidden) {
                                tv.setText(Integer.toString(x));
                                x-=1;
                                if(x>=0){
                                    handler.postDelayed(this,1000);
                                }

                            }
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myTask.execute(hidden);
                        }
                    },10000);

                }else {
                    far.setImageResource(R.drawable.far);
                    far.setVisibility(View.VISIBLE);
                    near.setVisibility(View.INVISIBLE);
                    hidden = false;
                    myTask.execute(hidden);
                    tv.setText(" ");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    class MyTask extends AsyncTask<Boolean,Void, Void>{


        @Override
        protected Void doInBackground(Boolean... params) {

            if(params[0] == true){
                if(state != 0 && x==0){
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(x==0){
                    mediaPlayer.start();
                    state++;
                }

            }else {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
            }
            return null;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        proxyManager.registerListener(proxyListener,proxy,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyTask myTask = new MyTask();
        hidden = false;
        myTask.execute(hidden);
        proxyManager.unregisterListener(proxyListener);
    }
}
