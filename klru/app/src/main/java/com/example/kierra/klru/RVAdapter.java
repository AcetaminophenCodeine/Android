package com.example.kierra.klru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.kierra.klru.MainActivity.context;

/**
 * Created by Hatter on 12/4/16.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    private ArrayList<ArrayList<Album>> urlArray;
    private ArrayList<String> categoryTitle;
    private  LayoutInflater layoutInf;

    public RVAdapter()
    {
        urlArray = new ArrayList<>();
        layoutInf = LayoutInflater.from(MainActivity.context);
        categoryTitle = new ArrayList<>();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder
    {
        private View container;
        final Context viewHolderContext = itemView.getContext();
        private TextView theTitle;
        private RecyclerView recyclerview_oneScroll;

        public RVViewHolder(View view)
        {
            super(view);
            container = view;
            theTitle = (TextView) view.findViewById(R.id.oneScroll_textview);
            recyclerview_oneScroll = (RecyclerView) view.findViewById(R.id.recycler_view_oneScroll);
        }
    }

    @Override
    public RVAdapter.RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View itemView = layoutInf.inflate(R.layout.one_scroll, parent, false);

        return new RVViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RVViewHolder holder, int position) {

        final int pos = position;
       // holder.theTitle = (TextView) holder.container.findViewById(R.id.oneScroll_textview);
        holder.theTitle.setText(categoryTitle.get(position));
        holder.theTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCategory(urlArray.get(pos));
            }
        });

       // holder.recyclerview_oneScroll = (RecyclerView) holder.container.findViewById(R.id.recycler_view_oneScroll);
        MyAdapter adapter = new MyAdapter(urlArray.get(position), MainActivity.context, VideoActivity.class,layoutInf);
       // holder.recyclerview_oneScroll.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setAutoMeasureEnabled(true);
        holder.recyclerview_oneScroll.setLayoutManager(layoutManager);
        holder.recyclerview_oneScroll.setAdapter(adapter);

    }

    public void add(ArrayList<Album> arrayList , char letter)
    {
        urlArray.add(arrayList);
        switch(letter) {
            case 'a':
                categoryTitle.add("Arts in Context");

                break;
            case 'c':
                categoryTitle.add("Central Texas Gardener");
                break;
            case 's':
                categoryTitle.add("SXSW Flashback");
                break;
            case 'j':
                categoryTitle.add("Juneteenth Jamboree");
                break;
            case 'o':
                categoryTitle.add("Overheard with Evan Smith");
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (urlArray == null)
            return 0;
        else
            return urlArray.size();
    }

    protected void goToCategory(ArrayList<Album> albArr)
    {
        Intent intent = new Intent(MainActivity.context,ArtsInContextActivity.class);
        intent.putParcelableArrayListExtra("array",albArr);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}

