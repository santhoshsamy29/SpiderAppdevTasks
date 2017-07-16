package com.example.application.f_task;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    ArrayList<CardView> img_list;
    int screenX;
    MainActivity.OnClickListener OnClickListener;

    public void setOnClickListener(MainActivity.OnClickListener OnClickListener){
        this.OnClickListener = OnClickListener;
    }

    public ImageAdapter(Context context,ArrayList<CardView> img_list,int screenX) {
        this.context = context;
        this.img_list = img_list;
        this.screenX = screenX;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setImageURI(img_list.get(position).image);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListener.onClick(img_list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return img_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            img.setMinimumWidth(screenX/3);
            img.setMaxWidth(screenX/3);

        }
    }
}
