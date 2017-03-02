package com.example.kierra.klru;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;


public class VideoFetch implements RateLimit.RateLimitCallback {
    public interface Callback {
        void fetchStart();

        void fetchComplete(String links);

        void fetchCancel(String links);
    }

    protected Callback callback = null;
    protected URL url;

    public VideoFetch(Callback callback, URL url, boolean front) {
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
        return url.equals(((XMLFetch) obj).url);
    }

    public void rateLimitReady() {
        // XXX write me: execute async downloader
        AsyncDownloader async = new AsyncDownloader();
        async.execute(url);
    }


    public class AsyncDownloader extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(String.valueOf(url)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements links = doc.select("iframe");
            for (Element link : links) {

                //arts in context episodes
                //TODO:ACTUALLY PARSE page for iframe video
                if (link.attr("id").contains("partnerPlayer")) {
                    String [] parts =link.attr("src").split("\\?");
                    return parts[0] + "?w=578&h=700&toolbar=true";
                } else if (link.attr("src").contains("vimeo") || (link.attr("src").contains("youtube"))){
                    //jj vimeo videos for year < 2013 && overheard youtube videos
                    return link.attr("src");
                }

            }
            return "";
        }



        @Override
        protected void onPostExecute(String link) {
            super.onPostExecute(link);

            if (link != null) {
                callback.fetchComplete(link);
            }

        }

        @Override
        protected void onCancelled(String link) {
            super.onCancelled(link);

            if (link != null) {
                callback.fetchCancel(link);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.fetchStart();
        }
    }
}

