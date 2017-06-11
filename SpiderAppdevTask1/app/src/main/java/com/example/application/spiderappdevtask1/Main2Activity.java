package com.example.application.spiderappdevtask1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle recieve = getIntent().getExtras();
        String dispMessage = recieve.getString("Message").toString();

        tv1 = (TextView)findViewById(R.id.dispMsg);
        tv1.setText(dispMessage);
    }
}
