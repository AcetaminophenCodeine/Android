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

    public static ArrayList<Album> urlArray;
    private MyAdapter mAdapter;
    private RecyclerView recyclerView;
    protected TheScrolls theScrolls;
    protected LayoutManager mLayoutManager;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        BitmapCache.cacheSize = maxMemory / 4;

        context = getApplicationContext();
        href = new TreeMap<>();
        URL uril=null;
        URL uril2=null;
        URL uril3=null;

        try
        {
            uril = new URL("http", "www.klru.org","/artsincontext/episodes");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        XMLFetch xmlArt = new XMLFetch(this,uril,true);

        try
        {
            uril2 = new URL("http", "www.klru.org","/juneteenth/episodes/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        XMLFetch xmlJuneteenth = new XMLFetch(this,uril2,true);

        try
        {
            uril3 = new URL("http", "www.klru.org","/sxswflashback/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        XMLFetch xmlSXSW = new XMLFetch(this,uril3,true);

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

        urlArray = new ArrayList<>();

    }

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(TreeMap<String, Album> links)
    {
        href=links;
        makeScorlingList();
        theScrolls = new TheScrolls();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.main_frame, theScrolls);
        // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
        ft.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commitAllowingStateLoss();

    }

    @Override
    public void fetchCancel(TreeMap<String, Album> links) {

    }

    protected void makeScorlingList()
    {
        for( Map.Entry<String,Album> entry : href.entrySet()) {
            String title = entry.getKey();
            Album album = entry.getValue();
            urlArray.add(album);
        }

        for( Album album : urlArray) {
            System.out.println(album.name + " => " + album.url);
        }
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

            case R.id.home:

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

