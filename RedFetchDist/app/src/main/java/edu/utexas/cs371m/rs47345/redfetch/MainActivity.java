package edu.utexas.cs371m.rs47345.redfetch;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.NavUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import java.io.IOException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.content.Context;


public class MainActivity extends AppCompatActivity implements URLFetch.Callback
{
    static public String AppName = "RedFetch";
    private ProgressBar progressBar;
    // Reddit json search will return up to 100 records, 25 by default
    // Only display a subset of those returned
    protected final int maxRedditRecords = 100;
    protected DynamicAdapter redditRecordAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;
    protected String userInput;
    public RecyclerView redditRecordsRecyclerView;
    ArrayList <RedditRecord> redditRecordsListMain;
    Bundle extras;
    SwipeDetector sd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //***********************************************************
        // XXX write me: call setContentView
        setContentView(R.layout.search_results);
        //***********************************************************

        //***********************************************************
        // XXX other initialization.
        progressBar = (ProgressBar) findViewById(R.id.netIndicator);
        rv_layout_mgr = new LinearLayoutManager(this);
        redditRecordsRecyclerView = (RecyclerView) findViewById(R.id.searchResults);
        redditRecordsListMain = new ArrayList<>();
        sd = new SwipeDetector();
        redditRecordsRecyclerView.addOnItemTouchListener(sd);
        redditRecordAdapter = new DynamicAdapter(sd,this);
        final Context context = this;
        rv_layout_mgr = new LinearLayoutManager(getApplicationContext());
        redditRecordsRecyclerView.setLayoutManager(rv_layout_mgr);
        redditRecordsRecyclerView.setAdapter(redditRecordAdapter);
        //***********************************************************

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/4th of the available memory for this memory cache.
        BitmapCache.cacheSize = maxMemory / 4;

        // Get the size of the display so we properly size bitmaps
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        BitmapCache.maxW = size.x;
        BitmapCache.maxH = size.y;


        // Listen for the enter key
        final EditText editText = (EditText) findViewById(R.id.searchTerm);

        editText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            userInput = editText.getText().toString();
                            redditRecordAdapter = new DynamicAdapter(sd,context);
                            rv_layout_mgr = new LinearLayoutManager(getApplicationContext());
                            redditRecordsRecyclerView.setLayoutManager(rv_layout_mgr);
                            redditRecordsRecyclerView.setAdapter(redditRecordAdapter);
                            newSearch();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    // https://www.reddit.com/dev/api#GET_search
    // myString = "/r/aww/search.json?q=%s&sort=hot&limit=100"
    //the code checks to make sure the term is well formatted (here?)
    protected void newSearch()
    {
        String q = "";
        String delims = "[ ]+";
        String[] tokens = userInput.split(delims);
        if (userInput.toCharArray().length < 512)
        {
            for(int i = 0 ; i < tokens.length - 1 ; i++)
            {
                q += tokens[i] + "+";
            }
            q += tokens[tokens.length-1];
        }

        String myString = "/r/aww/search.json?q=" + q + "&sort=hot&limit=100";

        URL searchURL = null;


        try {
            searchURL = new URL("https","www.reddit.com",myString);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        URLFetch urlFetch;
        if (searchURL != null) {
            progressBar.setVisibility(View.VISIBLE);
            urlFetch = new URLFetch(this, searchURL, false);
        }

           // Log.d(AppName, " searchURL is null");

    }

    // You will need to parse JSON
    // https://github.com/reddit/reddit/wiki/JSON

    // Finally, enjoy these simple functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings :
                return true;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void fetchStart() {}

    @Override public void fetchComplete(String result)
    {
        if (result != null)
        {
            progressBar.setVisibility(View.INVISIBLE);
            jsonObjtoRedditRecord(result);
            redditRecordAdapter.notifyDataSetChanged();
        }
    }

    @Override public void fetchCancel(String url) {}

    protected void jsonObjtoRedditRecord(String jsonStr)
    {
        //  Below is originally from:
        //          https://www.cs.utexas.edu/~witchel/371M/lectures/ut-json.pdf

        JSONObject jO = null;
        try {
            jO = new JSONObject(jsonStr);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        int jsonIndex = 0;

        if (jO == null) {
            ///Log.d(MainActivity.AppName, "JSONObject is null");
            return;
        }

        try {
            if (jO.isNull("data")) {
                //Log.d(MainActivity.AppName, "JSONObject data is null");
                return;
            }

            jO = jO.getJSONObject("data");
            JSONArray jA = jO.getJSONArray("children");

            while (jsonIndex < jA.length() && redditRecordAdapter.getItemCount() < maxRedditRecords)
            {
                jO = jA.getJSONObject(jsonIndex);

                if (jO.isNull("data"))
                {
                    jsonIndex++;
                    continue;
                }

                jO = jO.getJSONObject("data");

                // Make sure there is a thumbnail and an image url
                // Most thumbnails I see are jpg or png, but some urls are to imagur, which contain html
                if (    jO.isNull("thumbnail")
                        || (!jO.getString("thumbnail").endsWith("jpg") && !jO.getString("thumbnail").endsWith("png"))
                        || jO.isNull("url")
                        || (!jO.getString("url").endsWith("jpg") && !jO.getString("url").endsWith("png"))
                        || jO.isNull("title")) {
                    jsonIndex++;
                    continue;
                }

                String title = jO.getString("title");
                String thumbnailURL = jO.getString("thumbnail");
                String imageURL = jO.getString("url");

                if(thumbnailURL != "self") {
                    RedditRecord rd = new RedditRecord();
                    rd.title = title;
                    rd.thumbnailURL = new URL(thumbnailURL);
                    rd.imageURL = new URL(imageURL);
                    URLFetch thumbnailFetch = new URLFetch(this,  rd.thumbnailURL, false);
                    URLFetch imageFetch = new URLFetch(this,  rd.imageURL, false);
                    redditRecordAdapter.addItem(rd);
                }


                // Update loop index
                jsonIndex++;
            }
        }
        catch (Exception e)
        {
            //Log.d(MainActivity.AppName, " Reddit Records cannot be made or are not made correctly :o) ");
            e.printStackTrace();
        }

        // Above is originally from:
        //              https://www.cs.utexas.edu/~witchel/371M/lectures/ut-json.pdf



    }

   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK) {
            extras = data.getExtras();
        }
    }
}
