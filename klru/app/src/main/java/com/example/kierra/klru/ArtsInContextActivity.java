package com.example.kierra.klru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Hatter on 11/27/16.
 */

public class ArtsInContextActivity extends AppCompatActivity implements XMLFetch.Callback
{
    private AICAdapter adapter;
    protected RecyclerView recyclerView_Arts_in_Context;

    protected RecyclerView.LayoutManager layoutManager_AIC;
    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(TreeMap<String, Album> links) {

    }

    @Override
    public void fetchCancel(TreeMap<String, Album> links) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arts_in_context_layout);

        Intent i = getIntent();
        ArrayList<Album> a = i.getParcelableArrayListExtra("array");


        recyclerView_Arts_in_Context = (RecyclerView) findViewById(R.id.recycler_episodes);

        adapter = new AICAdapter(a, this, ContentActivity.class);
        layoutManager_AIC = new GridLayoutManager(this,3);
         recyclerView_Arts_in_Context.setLayoutManager(layoutManager_AIC);
        recyclerView_Arts_in_Context.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK) {

        }
    }
}
