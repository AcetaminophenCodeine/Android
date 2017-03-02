package com.example.kierra.klru;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;


public class ContentFetch implements RateLimit.RateLimitCallback {
    public interface Callback {
        void fetchStart();

        void fetchComplete(SubAlbum info);

        void fetchCancel(SubAlbum info);
    }

    protected Callback callback = null;
    protected URL url;

    public ContentFetch(Callback callback, URL url, boolean front) {
        this.callback = callback;
        this.url = url;
        if (front) {
            RateLimit.getInstance().addFront(this);
        } else {
            RateLimit.getInstance().add(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return url.equals(((ContentFetch) obj).url);
    }

    public void rateLimitReady() {
        // XXX write me: execute async downloader
        AsyncDownloader async = new AsyncDownloader();
        async.execute(url);
    }


    public class AsyncDownloader extends AsyncTask<Object, Object, SubAlbum> {
        @Override
        protected SubAlbum doInBackground(Object... params) {
            Document doc = null;
            SubAlbum sb = new SubAlbum();
            while(doc == null) {
                try {
                    doc = Jsoup.connect(String.valueOf(url)).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //div[id=episode-info]
            //Elements links = doc.select("iframe");
            Elements episode_description = doc.select("div[id=episode-description]");
            Elements ps = episode_description.select("p");
            String desc = ps.text();
            sb.description = desc;

            return sb;
        }



        @Override
        protected void onPostExecute(SubAlbum info) {
            super.onPostExecute(info);

            if (info != null) {
                callback.fetchComplete(info);
            }

        }

        @Override
        protected void onCancelled(SubAlbum info) {
            super.onCancelled(info);

            if (info != null) {
                callback.fetchCancel(info);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.fetchStart();
        }
    }
}

