package com.example.application.spiderappdevtask1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView myList;
    Button b1;
    Button b2;
    EditText et1;
    EditText et2;
    ArrayAdapter myAdapter;
    ArrayList<String> list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            list = savedInstanceState.getStringArrayList("Array");
        }

        myList = (ListView)findViewById(R.id.myList);
        et1 =(EditText)findViewById(R.id.eEt);
        b1 = (Button) findViewById(R.id.aButton);
        et2 =(EditText)findViewById(R.id.pEt);
        b2 = (Button) findViewById(R.id.rButton);

        myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        myList.setAdapter(myAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = et1.getText().toString();

                if (et1.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(MainActivity.this,"Enter a element to be added",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    list.add(x);
                    myAdapter.notifyDataSetChanged();

                }
                et1.setText("");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y = Integer.parseInt(et2.getText().toString());

                if(et2.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(MainActivity.this,"Enter a element to be removed",Toast.LENGTH_SHORT).show();
                }
                else if(y > myList.getCount()|| y < 1)
                {
                    Toast.makeText(MainActivity.this,"No item in that position",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    list.remove(y - 1);
                    myAdapter.notifyDataSetChanged();
                }
                et2.setText("");
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                String val = String.valueOf(parent.getItemAtPosition(position));
                i.putExtra("Message",val);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("Array",list);

    }
}
