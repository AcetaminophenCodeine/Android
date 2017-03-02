package com.example.kierra.klru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Hatter on 11/27/16.
 */

public class AICAdapter  extends RecyclerView.Adapter<AICAdapter.MyViewHolder>
        implements ContentFetch.Callback{

    private ArrayList<Album> urlArray;
    public Context contextStart;
    public Class classArrive;


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

    public AICAdapter(ArrayList<Album> urlArray , Context contextStart, Class classArrive) {
        this.urlArray = urlArray;
        this.contextStart = contextStart;
        this.classArrive = classArrive;
    }

    @Override
    public AICAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        return new AICAdapter.MyViewHolder(itemView);
    }

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(SubAlbum info) {

    }

    @Override
    public void fetchCancel(SubAlbum info) {

    }


    @Override
    public void onBindViewHolder(AICAdapter.MyViewHolder holder, int position) {

        TextView tv = (TextView) holder.container.findViewById(R.id.title);
        ImageView iv = (ImageView) holder.container.findViewById(R.id.thumbnail);

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        Album entry = urlArray.get(position);

        holder.adapterTitleTextView.setText(entry.name);
        if(entry.thumbnail != null) {
            Bitmap bm = BitmapCache.getInstance().getBitmap(entry.thumbnail.toString());
            holder.adapterThumbnailImageView.setImageBitmap(bm);
        }
        else
        {
            holder.adapterThumbnailImageView.setImageBitmap(BitmapCache.defaultThumbnailBitmap);
        }

        //VideoFetch vf = new VideoFetch(contextStart,entry.url,true);
    }

    @Override
    public int getItemCount() {
        return urlArray.size();
    }

}
