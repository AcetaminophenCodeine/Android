package com.example.kierra.klru;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hatter on 11/27/16.
 */

public class VideoActivity extends AppCompatActivity implements VideoFetch.Callback {

    private WebView myWebView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        String intentURL = intent.getStringExtra("url");
        myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = myWebView.getSettings();

        myWebView.setBackgroundColor(Color.BLACK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        if(!intentURL.contains("partnerplayer")) {
            URL videoURL = null;
            try {
                videoURL = new URL(intentURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            VideoFetch video = new VideoFetch(this, videoURL, true);
        } else {
            myWebView.loadData("<iframe style=\"width:600px; height:220px;\" src=\"" + intentURL + "\"></iframe>", "text/html",
                    "utf-8");
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myWebView.destroy();
    }


    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete(String links) {
        myWebView.loadData("<iframe style=\"width:600px; height:220px;\" src=\"" + links + "\"></iframe>", "text/html",
                "utf-8");
    }

    @Override
    public void fetchCancel(String links) {

    }
}
