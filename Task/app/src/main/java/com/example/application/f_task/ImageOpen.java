package com.example.application.f_task;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class ImageOpen extends AppCompatActivity {

    MyDataBase dataBase = new MyDataBase(this);
    ArrayList<CardView> imgs_list = new ArrayList<>();
    ImageButton right,left;
    private int pos;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageopen);

        imgs_list = dataBase.getData();

        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("Image");
        //Uri imgUri = Uri.parse(img);

        imageView = (ImageView)findViewById(R.id.img_open);
        imageView.setImageURI(imgs_list.get(pos).image);
        left = (ImageButton)findViewById(R.id.prev_img);
        right = (ImageButton)findViewById(R.id.next_img);


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos < imgs_list.size()-1){
                    pos +=1;
                    imageView.setImageURI(imgs_list.get(pos).image);
                }else{
                    Toast.makeText(ImageOpen.this,"No Images Left",Toast.LENGTH_SHORT).show();
                }


            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos > 0 ){
                    pos -=1;
                    imageView.setImageURI(imgs_list.get(pos).image);
                }else {
                    Toast.makeText(ImageOpen.this,"No Images Left",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

}
