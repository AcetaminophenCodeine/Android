package com.example.kierra.klru;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

/**
 * Created by Hatter on 11/27/16.
 */

public class ContentActivity extends AppCompatActivity implements ContentFetch.Callback {

    WebView webview = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        Intent intent = getIntent();
        Album episode = intent.getParcelableExtra("albumInfo");
        URL videoURL = null;
        try {
            videoURL = new URL(intent.getStringExtra("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ContentFetch content = new ContentFetch(this,videoURL,true);
        webview = new WebView(this);

        setContentView(webview);
        webview.setWebChromeClient(new WebChromeClient() {

        });
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

    }

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(SubAlbum info)
    {
        //getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webview.loadData("<iframe src=\"" + info + "\"></iframe>", "text/html",
                "utf-8");
    }

    @Override
    public void fetchCancel(SubAlbum info) {

    }
}
