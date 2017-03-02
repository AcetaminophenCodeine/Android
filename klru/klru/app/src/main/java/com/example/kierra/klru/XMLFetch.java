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
import java.util.TreeMap;

public class XMLFetch implements RateLimit.RateLimitCallback {
    public interface Callback {
        void fetchStart();

        void fetchComplete(TreeMap<String, Album> links);

        void fetchCancel(TreeMap<String, Album> links);
    }

    protected Callback callback = null;
    protected URL url;

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

            //if(url == null)
                //System.out.println("url null");

            TreeMap<String,Album> tree = new TreeMap<>();
            String stringUrl = url.toString();

            Document doc = null;

            while(doc == null) {
                try {
                    doc = Jsoup.connect(String.valueOf(url)).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (stringUrl.contains("artsincontext"))
            {
                tree = aicTreeMap(doc);
            }
            else if (stringUrl.contains("juneteenth"))
            {
                tree = jjTreeMap(doc);
            }
            else if (stringUrl.contains("sxsw")) {
                tree = sxswTreeMap(doc);
            } else if (stringUrl.contains("ctg")){
                tree = ctgTreeMap(doc);
            } else if (stringUrl.contains("overheard")){
                tree = overheardTreeMap(doc);
            }

            return tree;
        }

        @Override
        protected void onPostExecute(TreeMap<String, Album> links) {
            super.onPostExecute(links);

            if (links != null) {
                callback.fetchComplete(links);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }

        @Override
        protected void onCancelled(TreeMap<String, Album> links) {
            super.onCancelled(links);

            if (links != null) {
                callback.fetchCancel(links);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.fetchStart();
        }

        protected TreeMap<String, Album> aicTreeMap(Document doc)
        {
            TreeMap<String, Album> urlLinks = new TreeMap<>();
            String title = "";
            String title2 = null;

            Elements episode_items_list = doc.select("div[class=episode-item]");
            String regex = "\\s*\\bPermanent Link to\\b\\s*";

            for (Element item : episode_items_list) {
                Elements img = item.select("img");
                Elements href = item.select("a[href]");

                ArrayList<URL> images = new ArrayList<>();
                for (Element e : img) {
                    try {
                        images.add(new URL(e.attr("src")));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                Element href0 = href.first();

                title2 = title + href0.attr("title").replaceAll(regex, "").trim();
                Element img0 = img.first();
                URL thumbnailUrl = null;

                try {
                    thumbnailUrl = new URL(img0.attr("src"));
                    HttpURLConnection connection = (HttpURLConnection) thumbnailUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(input);
                    BitmapCache.getInstance().setBitmap(thumbnailUrl.toString(), bm);
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    Album album = new Album();
                    album.name = title2;
                    album.url = new URL(href0.attr("href"));
                    album.thumbnail = thumbnailUrl;
                    album.images = images;
                    urlLinks.put("artsincontext" + title2, album);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            return urlLinks;
        }

        protected TreeMap<String, Album> ctgTreeMap(Document doc)
        {
            TreeMap<String, Album> urlLinks = new TreeMap<>();
            Elements episode_items_list = doc.select("div[class=item-new]");

            for (Element item : episode_items_list)
            {
                Elements img = item.select("img");
                Elements href = item.select("a[href]");
                Elements hTitle = item.select("h4");

                ArrayList<URL> images = new ArrayList<>();
                for (Element e : img) {
                    try {
                        images.add(new URL(e.attr("src")));
                        //System.out.println("e: " + e.attr("src"));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                Element img0 = img.first();
                Element href0 = href.first();
                Element hTitle1 = hTitle.first();

                URL thumbnailUrl = null;
                InputStream input = null;
                try {
                    thumbnailUrl = new URL(img0.attr("src"));
                    //System.out.println("img0: " + img0.attr("src"));
                    //System.out.println("thumbnail: " + thumbnailUrl.toString() );
                    HttpURLConnection connection = (HttpURLConnection) thumbnailUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(input);
                    BitmapCache.getInstance().setBitmap(thumbnailUrl.toString(), bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    Album album = new Album();
                    album.name = hTitle1.text();
                    String jUrl = href0.attr("href");
                    album.url = new URL(jUrl);
                    album.thumbnail = thumbnailUrl;
                    album.images = images;
                    urlLinks.put("ctg" + hTitle.text(), album);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            return urlLinks;
        }

        protected TreeMap<String, Album> sxswTreeMap(Document doc)
        {
            Elements episode_items_list = doc.select("div[class=clip]");

            TreeMap<String, Album> urlLinks = new TreeMap<>();
            String title2 = null;


            for (Element item : episode_items_list)
            {
                Elements img = item.select("img");
                Elements href = item.select("a[href]");
                Element href0 = href.first();
                ArrayList<URL> images = new ArrayList<>();
                for (Element e : img) {
                    try {
                        images.add(new URL("http://www.klru.org/sxswflashback/" + e.attr("src")));
                        //System.out.println("e: " + e.attr("src"));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                Element img0 = img.first();
                URL thumbnailUrl = null;

                InputStream input = null;
                try {
                    thumbnailUrl = new URL("http://www.klru.org/sxswflashback/" +img0.attr("src"));
                    //System.out.println("img0: " + img0.attr("src"));
                    //System.out.println("thumbnail: " + thumbnailUrl.toString() );
                    HttpURLConnection connection = (HttpURLConnection) thumbnailUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(input);
                    BitmapCache.getInstance().setBitmap(thumbnailUrl.toString(), bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try
                {
                    Album album = new Album();
                    String vidNum = href0.attr("href").replaceAll("[^0-9]", "");
                    title2 = img0.attr("alt");
                    if(title2.contains("2015")){
                        album.url = new URL("http://video.klru.tv/widget/partnerplayer/2365529626?w=970&h=546&autoplay=false&start=0&end=0&chapterbar=true&toolbar=true&endscreen=false");
                    } else{
                        album.url = new URL("http://video.klru.tv/widget/partnerplayer/" + vidNum + "?w=970&h=546&autoplay=false&start=0&end=0&chapterbar=true&toolbar=true&endscreen=false");
                    }
                    album.name = title2;
                    //System.out.println("title2: " + title2 );
                   // System.out.println("url: " + album.url );
                    album.thumbnail = thumbnailUrl;
                    album.images = images;
                    urlLinks.put("sxsw" + title2, album);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            return urlLinks;
        }

        protected TreeMap<String, Album> jjTreeMap(Document doc)
        {

            TreeMap<String, Album> urlLinks = new TreeMap<>();
            Elements episode_items_list = doc.select("div[class=asset]");
            String regex = "../episode/";
            String title = "Juneteenth ";
            String title2 = null;

            for (Element item : episode_items_list)
            {
                Elements img = item.select("img");
                Elements href = item.select("a[href]");

                ArrayList<URL> images = new ArrayList<>();
                for (Element e : img) {
                    try {
                        images.add(new URL(e.attr("src")));
                        //System.out.println("e: " + e.attr("src"));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                Element img0 = img.first();
                Element href0 = href.first();

                title2 = title + href0.attr("href").replaceAll(regex, "").trim();

                URL thumbnailUrl = null;
                InputStream input = null;
                try {
                    thumbnailUrl = new URL(img0.attr("src"));
                    //System.out.println("img0: " + img0.attr("src"));
                    //System.out.println("thumbnail: " + thumbnailUrl.toString() );
                    HttpURLConnection connection = (HttpURLConnection) thumbnailUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(input);
                    BitmapCache.getInstance().setBitmap(thumbnailUrl.toString(), bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    Album album = new Album();
                    album.name = title2;
                    String jUrl = "http://www.klru.org/juneteenth" + href0.attr("href").replaceAll("\\.", "");
                    album.url = new URL(jUrl);
                    album.thumbnail = thumbnailUrl;
                    album.images = images;
                    urlLinks.put("juneteenth" + title2, album);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            return urlLinks;
        }

        protected TreeMap<String, Album> overheardTreeMap(Document doc)
        {
            TreeMap<String, Album> urlLinks = new TreeMap<>();
            Elements episode_items_list = doc.select("div[class=thumbnail]");
            String title2 = null;
            String regex = "http://www.klru.org/overheard/episode/";

            for (Element item : episode_items_list)
            {
                Elements img = item.select("img");
                Elements href = item.select("a[href]");

                ArrayList<URL> images = new ArrayList<>();
                for (Element e : img) {
                    try {
                        images.add(new URL(e.attr("src")));
                        //System.out.println("e: " + e.attr("src"));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                Element img0 = img.first();
                Element href0 = href.first();

                title2 = href0.attr("href").replaceAll(regex, "").replaceAll("-", " ").replace("/","").trim();

                URL thumbnailUrl = null;
                InputStream input = null;
//                    thumbnailUrl = new URL(img0.attr("src"));
//                    System.out.println("img0: " + img0.attr("src"));
//                    System.out.println("thumbnail: " + thumbnailUrl.toString() );
                //HttpURLConnection connection = (HttpURLConnection) thumbnailUrl.openConnection();
                //connection.setDoInput(true);
                //connection.connect();
                // input = connection.getInputStream();
                //Bitmap bm = BitmapFactory.decodeStream(input);
                BitmapCache.getInstance().setBitmap(href0.attr("href"), BitmapCache.defaultThumbnailBitmap);


                try {

                    Album album = new Album();
                    album.name = title2;
                    String jUrl = href0.attr("href");
                    album.url = new URL(jUrl);
                    album.thumbnail = album.url;
                    album.images = images;
                    urlLinks.put("overheard" + title2, album);

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
                return s.substring(0, width - 1) + ".";
            else
                return s;
        }



    }
}

