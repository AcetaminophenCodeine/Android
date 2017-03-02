package com.example.kierra.klru;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hatter on 11/16/16.
 */

public class TheScrolls extends Fragment{

    protected View myRootView;
    private ArrayList<Album> urlArray;
    protected TreeMap<String,URL> href;
    private MyAdapter mAdapter;
    protected RecyclerView recyclerView_Arts_in_Context;
    protected RecyclerView recyclerView_CTG;
    protected RecyclerView recyclerView_JJ;
    protected RecyclerView recyclerView_SXSW;
    protected RecyclerView recyclerView_overheard;
    protected TextView tv1;

    protected LayoutManager layoutManager_AIC;
    protected LayoutManager layoutManager_CTG;
    protected LayoutManager layoutManager_JJ;
    protected LayoutManager layoutManager_SXSW;
    protected LayoutManager layoutManager_overheard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.the_scrolls, container, false);
        myRootView = v;
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = myRootView;

        recyclerView_Arts_in_Context = (RecyclerView) v.findViewById(R.id.recycler_view_Arts_in_Context);
        recyclerView_CTG = (RecyclerView) v.findViewById(R.id.recycler_view_Central_Texas_Gardener);
        recyclerView_JJ = (RecyclerView) v.findViewById(R.id.recycler_view_Juneteenth_Jamboree);
        recyclerView_SXSW = (RecyclerView) v.findViewById(R.id.recycler_view_SXSW_Flashback);
        recyclerView_overheard = (RecyclerView) v.findViewById(R.id.recycler_view_Overheard_with_Evan_Smith);
        tv1 = (TextView) v.findViewById(R.id.arts_in_context_textview);
        tv1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.context,ArtsInContextActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("urlArray", urlArray);
                intent.putExtras(b);
                MainActivity.context.startActivity(intent);
            }
        });

        //  XXXX pass activity here
        mAdapter = new MyAdapter(MainActivity.urlArray, MainActivity.context, VideoActivity.class);
        // *****************
        layoutManager_AIC = new LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Arts_in_Context.setLayoutManager(layoutManager_AIC);
        recyclerView_Arts_in_Context.setAdapter(mAdapter);


        /*
        mAdapter = new MyAdapter(urlArray);
        layoutManager_CTG = new LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView_CTG.setLayoutManager(layoutManager_CTG);
        recyclerView_CTG.setAdapter(mAdapter);

        mAdapter = new MyAdapter(urlArray);
        layoutManager_JJ = new LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView_JJ.setLayoutManager(layoutManager_JJ);
        recyclerView_JJ.setAdapter(mAdapter);

        mAdapter = new MyAdapter(urlArray);
        layoutManager_SXSW = new LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView_SXSW.setLayoutManager(layoutManager_SXSW);
        recyclerView_SXSW.setAdapter(mAdapter);

        mAdapter = new MyAdapter(urlArray);
        layoutManager_overheard = new LinearLayoutManager(MainActivity.context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView_overheard.setLayoutManager(layoutManager_overheard);
        recyclerView_overheard.setAdapter(mAdapter);
        */

    }
}
