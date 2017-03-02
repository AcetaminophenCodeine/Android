package com.example.kierra.klru;

/**
 * Created by Kierra on 11/8/2016.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class XMLFetch implements RateLimit.RateLimitCallback {
    public interface Callback {
        void fetchStart();

        void fetchComplete(TreeMap<String, Album> links);

        void fetchCancel(TreeMap<String, Album> links);
    }

    protected Callback callback = null;
    protected URL url;
    public static String trimmed;
    protected TreeMap<String,Album> episodeLinks;

    public XMLFetch(Callback callback, URL url, boolean front) {
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


    public class AsyncDownloader extends AsyncTask<URL, Integer, TreeMap<String, Album>> {
        @Override
        protected TreeMap<String, Album> doInBackground(URL... params) {
            Document doc = null;
            TreeMap<String, Album> urlLinks = new TreeMap<>();
            String title = null;
            String regex = null;
            String stringUrl = url.toString();
            Elements episode_items = null;
            String title2 = null;
            try {
                doc = Jsoup.connect(String.valueOf(url)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (stringUrl.contains("artsincontext")) {
                //arts in context
                episode_items = doc.select("div[class=episode-item]");

                regex = "\\s*\\bPermanent Link to\\b\\s*";
                //title = title.replaceAll(regex, "").trim();
            } else if (stringUrl.contains("juneteenth")) {
                //juneteenth
                episode_items = doc.select("div[class=asset]");
                regex = "\\s*\\bhttp://www.klru.org/juneteenth/episode/\\b\\s*";
                title = "Juneteenth ";
            } else if (stringUrl.contains("sxsw")) {
                //swsx
                episode_items = doc.select("div[class=clip]");
            }
            //print("\nLinks: (%d)", links.size());
            for (Element item : episode_items )
            {
                Elements img = item.select("img");
                Elements href = item.select("a[href]");
                //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
                Element img0 = img.first();
                Element href0 = href.first();
                if (regex == null){
                    title2 = title + href0.attr("title").trim();

                }else {
                    title2 = title + href0.attr("title").replaceAll(regex, "").trim();
                }
                URL thumbnailUrl= null;
                try {
                    thumbnailUrl = new URL(img0.attr("src"));
                    HttpURLConnection connection = (HttpURLConnection) thumbnailUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(input);
                    BitmapCache.getInstance().setBitmap(thumbnailUrl.toString(),bm);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                try {
                    System.out.println(img0.attr("src"));
                    if(stringUrl.contains("juneteenth")){
                        String jUrl="http://www.klru.org/juneteenth/"+href0.attr("href").replaceAll(".","");
                        urlLinks.put(title2,new Album(title,new URL(jUrl), thumbnailUrl));
                    } else {

                        urlLinks.put(title2, new Album(title2, new URL(href0.attr("href")), thumbnailUrl));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }

            return urlLinks;
        }

        private void print(String msg, Object... args) {
            System.out.println(String.format(msg, args));
        }

        private String trim(String s, int width) {
            if (s.length() > width)
                return s.substring(0, width-1) + ".";
            else
                return s;
        }


        @Override
        protected void onPostExecute(TreeMap<String, Album> links)
        {
            super.onPostExecute(links);

            if(links != null)
            {
                callback.fetchComplete(links);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
           // setProgressPercent(progress[0]);
        }

        @Override
        protected void onCancelled(TreeMap<String, Album> links)
        {
            super.onCancelled(links);

            if(links != null)
            {
                callback.fetchCancel(links);
            }

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            callback.fetchStart();
        }
    }
}
