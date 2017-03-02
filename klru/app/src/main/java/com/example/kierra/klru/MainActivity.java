package com.example.kierra.klru;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity implements XMLFetch.Callback,
                                    NavigationView.OnNavigationItemSelectedListener{

    public TreeMap<String,Album> href;
    protected Menu drawerMenu;
    protected ActionBarDrawerToggle toggle;
    protected char categoryLetter;
    private ArrayList<Album> arrayList;
    public static Context context;
    private RVAdapter mAdapter;
    protected RecyclerView recyclerView_theScrolls;
    protected LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        BitmapCache.cacheSize = maxMemory / 4;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        BitmapCache.maxH = displaymetrics.heightPixels;
        BitmapCache.maxW = displaymetrics.widthPixels;

        arrayList = new ArrayList<>();
        context = getApplicationContext();
        href = new TreeMap<>();
        URL uril1 = null;
        URL uril2 = null;
        URL uril3 = null ;
        URL uril4 = null;
        URL uril5 = null ;

        try
        {
            uril1 = new URL("http", "www.klru.org","/artsincontext/episodes");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try
        {
            uril2 = new URL("http", "www.klru.org","/ctg/episodes/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try
        {
            uril3 = new URL("http", "www.klru.org","/sxswflashback/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try
        {
            uril4 = new URL("http", "www.klru.org","/juneteenth/episodes/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try
        {
            uril5 = new URL("http", "www.klru.org","/overheard/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        XMLFetch xmlOverHeard = new XMLFetch(this, uril5, true);
        XMLFetch xmlJuneteenth = new XMLFetch(this,uril4,true);
        XMLFetch xmlSXSW = new XMLFetch(this,uril3,true);
        XMLFetch xmlCTG = new XMLFetch(this,uril2,true);
        XMLFetch xmlArt = new XMLFetch(this,uril1,true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerMenu = navigationView.getMenu();

        recyclerView_theScrolls = (RecyclerView) findViewById(R.id.recycler_view_in_the_scrolls);
        mAdapter = new RVAdapter();
        layoutManager = new LinearLayoutManager(MainActivity.context);
        recyclerView_theScrolls.setLayoutManager(layoutManager);
        recyclerView_theScrolls.setAdapter(mAdapter);

    }

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(TreeMap<String, Album> links)
    {
        href=links;
        ArrayList<Album> arrayList = makeScorlingList();
        mAdapter.add(arrayList,categoryLetter);

    }

    @Override
    public void fetchCancel(TreeMap<String, Album> links) {

    }

    protected ArrayList<Album> makeScorlingList()
    {
        ArrayList<Album> theArray = new ArrayList<>();
        Map.Entry<String,Album> firstItem = href.firstEntry();
        String categoryName = firstItem.getKey();
        categoryLetter = categoryName.charAt(0);

        for(Map.Entry<String,Album> item: href.entrySet())
        {
            Album album;
            album = item.getValue();
            theArray.add(album);
        }

      /*  for( Album album : aicArray) {
            System.out.println(album.name + " => " + album.url);
        }*/

        return theArray;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK) {

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("main", "menu option selected");

        switch (id) {
            case R.id.new_arrival_1:

            case R.id.new_arrival_2:

            case R.id.new_arrival_3:

            case R.id.new_arrival_4:

            case R.id.new_arrival_5:

            case R.id.about:

            case R.id.help:

            case R.id.contact_us:

            case R.id.privacy_policy:

            default:
                break;

        }
        return true;
    }

}

