package edu.utexas.cs371m.rs47345.redfetch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.net.URL;

public class OnePost extends AppCompatActivity
{
    ArrayList <RedditRecord> al;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_post);

        Intent i =  getIntent();
        Bundle callingBundle = i.getExtras();

        String title = callingBundle.getString("title");
        Serializable s = callingBundle.getSerializable("ArrayList");
        al = (ArrayList<RedditRecord>) s;
        Serializable Url = callingBundle.getSerializable("imageURL");
        URL imageUrl = (URL) Url;

        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(title);

        ImageView iv = (ImageView) findViewById(R.id.image);
        Bitmap bm = BitmapCache.getInstance().getBitmap(imageUrl.toString());

        if (bm != null)
            iv.setImageBitmap(bm);

    }

}
