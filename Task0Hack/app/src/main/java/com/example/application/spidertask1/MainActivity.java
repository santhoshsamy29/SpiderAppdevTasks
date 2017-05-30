package com.example.application.spidertask1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text1;
    public int x=0;
    public static final String PREF = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView)findViewById(R.id.text1);
        text1.setText(Integer.toString(x));

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x+=1;
                text1.setText(Integer.toString(x));
            }
        });

        Button button2 = (Button)findViewById(R.id.reset);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=0;
                text1.setText(Integer.toString(x));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Number",x);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        x=preferences.getInt("Number",0);

        text1 = (TextView)findViewById(R.id.text1);
        text1.setText(Integer.toString(x));
    }
}
