package edu.utexas.cs371m.witchel.fcasynctask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class URLFetch implements RateLimit.RateLimitCallback {
    public interface FetchCallback {
        void fetchStart();
        void fetchComplete(Bitmap result);
        void fetchCancel(Bitmap result);
    }

    protected FetchCallback fetchCallback = null;
    protected URL url;

    public URLFetch(FetchCallback fetchCallback, URL url)
    {
        this.fetchCallback = fetchCallback;
        this.url = url;
        RateLimit.getInstance().addFront(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return url.equals(((URLFetch) obj).url);
    }

    public void rateLimitReady() {
        // XXX write me
        AsyncDownloader downloader = new AsyncDownloader();
        downloader.execute(url);

    }

    public class AsyncDownloader extends AsyncTask <URL, Integer, Bitmap>{ // XXX Write me

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);

            if(bitmap != null)
            {
                fetchCallback.fetchComplete(bitmap);
            }

        }

        @Override
        protected void onCancelled(Bitmap bitmap)
        {
            super.onCancelled(bitmap);

            if(bitmap != null)
            {
                fetchCallback.fetchCancel(bitmap);
            }

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            fetchCallback.fetchStart();
        }


        // Interface says we take an array of URLs, but we only process one.
        @Override
        protected Bitmap doInBackground(URL... urls)
        {
            int h = 535;
            int w = 535;

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bm);

            Paint darkPaint = new Paint();
            darkPaint.setColor(Color.rgb(50, 50, 50));

            Paint lightPaint = new Paint();
            lightPaint.setColor(Color.rgb(200, 200, 200));

            canvas.drawCircle(w / 2, h / 2, h / 2, darkPaint);
            canvas.drawCircle(w / 2, h / 2, h / 4, lightPaint);

            return bm;
        }

    }
}