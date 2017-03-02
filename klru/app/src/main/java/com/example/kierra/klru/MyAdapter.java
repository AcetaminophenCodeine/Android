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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
                        implements XMLFetch.Callback{

    private ArrayList<Album> urlArray;
    public Context contextStart;
    public Class classArrive;

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(TreeMap<String, Album> links) {
        notifyDataSetChanged();
    }

    @Override
    public void fetchCancel(TreeMap<String, Album> links) {

    }

    public void addItem(Album a) {
        urlArray.add(a);
        notifyDataSetChanged();
    }

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

    public MyAdapter(ArrayList<Album> urlArray , Context contextStart, Class classArrive) {
        this.urlArray = urlArray;
        this.contextStart = contextStart;
        this.classArrive = classArrive;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

       /* itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView titleView = (TextView) view.findViewById(R.id.title);
                Toast.makeText(view.getContext(),
                        "You clicked " + titleView.getText(),
                        Toast.LENGTH_SHORT).show();
                goToArtsInContext();
            }
        });*/

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TextView tv = (TextView) holder.container.findViewById(R.id.title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        Album entry = urlArray.get(position);
        holder.adapterTitleTextView.setText(entry.name);
        Bitmap bm = BitmapCache.getInstance().getBitmap(entry.thumbnail.toString());
        holder.adapterThumbnailImageView.setImageBitmap(bm);

        tv.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v) {
                                            goToVideo();
                                        }
                                    });

    }

    @Override
    public int getItemCount() {
        return urlArray.size();
    }

    protected void goToVideo()
    {
        Intent intent = new Intent(contextStart,classArrive);
        intent.putExtra("albumInfo",urlArray.get(currentPosition));
        String url = urlArray.get(currentPosition).url.toString();
        intent.putExtra("url", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contextStart.startActivity(intent);
    }
}

