package edu.utexas.cs371m.witchel.fcasynctask;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

// XXX MainActivity should implement something
public class MainActivity extends AppCompatActivity  implements URLFetch.FetchCallback{

    ImageView iv;

    public String AppName = "FCAsyncTask";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.imageView);

        EditText editText = (EditText) findViewById(R.id.searchTerm);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            doFetch();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    protected void doFetch() {
        // XXX Write me.
        // Remember to clear the imageview
        iv.setImageResource(0);

        URL url = null;
        try {
            url = new URL("https://google.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

            URLFetch urlfetch = new URLFetch(this, url);

    }

    // XXX Write me


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchStart() {

    }

    public void fetchComplete(Bitmap result) {
            iv.setImageBitmap(result);
    }

    public void fetchCancel(Bitmap result) {

    }
}
