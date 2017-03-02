package edu.utexas.cs371m.rs47345.redfetch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.MalformedURLException;



public class URLFetch implements RateLimit.RateLimitCallback
{
    public interface Callback
    {
        void fetchStart();
        void fetchComplete(String result);
        void fetchCancel(String url);
    }

    protected Callback callback = null;
    protected URL url;

    public URLFetch(Callback callback, URL url, boolean front)
    {
        this.callback = callback;
        this.url = url;
        if( front ) {
            RateLimit.getInstance().addFront(this);
        } else {
            RateLimit.getInstance().add(this);
        }
    }

    // checks for no duplicate URLFetch objects,
    // if url the same objects are the same
    // RateLimit does not allow duplicates
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return url.equals(((URLFetch)obj).url);
    }

    public void rateLimitReady()
    {
        // XXX write me: execute async downloader

        AsyncDownloader downloader = new AsyncDownloader();
        downloader.execute(url);
    }

    public class AsyncDownloader extends AsyncTask<URL, Integer, String> {
        // XXX Write me

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if (str != null) {
                callback.fetchComplete(str);
            }
        }

        @Override
        protected void onCancelled(String str) {
            super.onCancelled(str);

            if (str != null) {
                callback.fetchCancel(str);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.fetchStart();
        }


        protected String tryConnection(URL theUrl)
        {
            HttpURLConnection urlConn = null;
            boolean httpOk = false;

            try {
                //Log.d(MainActivity.AppName, "HttpURLConnection is happening! :o) ");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestProperty("User-Agent",
                        "android:edu.utexas.cs371m.rs47345.edu.utexas.cs371m.rs47345.redfetch:v1.0 " +
                                "(by /u/rs47345)");
                urlConn.connect();

                if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    httpOk = true;
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent",
                            "android:edu.utexas.cs371m.rs47345.edu.utexas.cs371m.rs47345.redfetch:v1.0 " +
                                    "(by /u/rs47345)");
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                   // below from http://stackoverflow.com/questions/11498013/android-httpurlconnection-getheaderfield
                    String contentType = connection.getHeaderField("Content-Type");
                    boolean image = contentType.startsWith("image/");

                    if(image)
                    {
                        // above from http://stackoverflow.com/questions/11498013/android-httpurlconnection-getheaderfield
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        BitmapCache.getInstance().setBitmap(theUrl.toString(), myBitmap);

                        if (BitmapCache.getInstance().getBitmap(theUrl.toString()) == null) {
                            BitmapCache.getInstance().setBitmap(theUrl.toString(),
                                    BitmapCache.errorImageBitmap);
                            return null;
                        }
                    }
                    else
                    {
                        JSONObject jo = readJSONFromURLInputStream(input);
                        return jo.toString();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected String doInBackground(URL...urls)
        {
            return tryConnection(urls[0]);

        }


        protected JSONObject readJSONFromURLInputStream(InputStream inputStream)
        {
            BufferedReader streamReader = null;
            try {
                streamReader=new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr = null;
            try {
                inputStr = streamReader.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            while ( inputStr != null)
            {
                responseStrBuilder.append(inputStr);
                try {
                    inputStr = streamReader.readLine();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

            JSONObject json = null;

            try {
                json = new JSONObject(responseStrBuilder.toString());
            }
            catch(JSONException je)
            {
                je.printStackTrace();
            }

            return json;
        }
    }
}
