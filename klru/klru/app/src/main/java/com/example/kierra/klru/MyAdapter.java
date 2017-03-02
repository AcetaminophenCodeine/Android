package com.example.kierra.klru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Created by Hatter on 11/14/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Album> urlArray;
    public Context contextStart;
    public Class classArrive;
    private int mRowIndex = -1;
    private LayoutInflater layoutInflater;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView adapterTitleTextView;
        public ImageView adapterThumbnailImageView;
        public View container;

        public MyViewHolder(View view)
        {
            super(view);
            adapterTitleTextView = (TextView) view.findViewById(R.id.title);
            adapterThumbnailImageView = (ImageView) view.findViewById(R.id.thumbnail);
            container = view;
        }
    }

    public MyAdapter(ArrayList<Album> urlArray , Context contextStart, Class classArrive, LayoutInflater layoutInflater) {
        this.urlArray = urlArray;
        this.contextStart = contextStart;
        this.classArrive = classArrive;
        this.layoutInflater =layoutInflater;
    }


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final Album entry = urlArray.get(position);
        holder.adapterTitleTextView.setText(entry.name);
        holder.adapterTitleTextView.setTextSize(16);

        if (entry.thumbnail != null) {
            Bitmap bm = BitmapCache.getInstance().getBitmap(entry.thumbnail.toString());
            holder.adapterThumbnailImageView.setImageBitmap(bm);
        }
        final int pos = position;
        holder.adapterTitleTextView.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v) {
                                            goToVideo(pos);
                                        }
                                    });
        holder.adapterThumbnailImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                goToVideo(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return urlArray.size();
    }

    protected void goToVideo(int currentPosition)
    {
        Intent intent = new Intent(MainActivity.context,VideoActivity.class);
        intent.putExtra("albumInfo",urlArray.get(currentPosition));
        String url = urlArray.get(currentPosition).url.toString();
        intent.putExtra("url", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contextStart.startActivity(intent);
    }
}

