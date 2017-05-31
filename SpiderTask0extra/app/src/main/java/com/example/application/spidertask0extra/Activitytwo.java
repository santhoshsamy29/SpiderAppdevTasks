package com.example.application.spidertask0extra;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Activitytwo extends AppCompatActivity {

    TextView tv1;
    private int x;
    RelativeLayout layout;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_two);

        Bundle recieve = getIntent().getExtras();
        if (recieve == null)
        {
            return;
        }
        x = recieve.getInt("itimer");


        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                tv1 = (TextView) findViewById(R.id.tv1);
                tv1.setText(Integer.toString(x));
                if(x<=10)
                {
                    layout = (RelativeLayout)findViewById(R.id.layout2);
                    layout.setBackgroundColor(Color.RED);
                    mp = MediaPlayer.create(getApplicationContext(),R.raw.beep);
                    mp.start();
                }
                x -= 1;
                if (x >= 0)
                {
                    handler.postDelayed(this, 1000);
                }
            }
        });

    }


}
