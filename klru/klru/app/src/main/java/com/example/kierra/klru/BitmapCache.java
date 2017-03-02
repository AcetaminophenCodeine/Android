package com.example.kierra.klru;

/**
 * Created by Hatter on 11/28/16.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;



public class BitmapCache
{
    // We assume main activity will set this before getInstance() is called
    public static int cacheSize = 0;
    // Clients (i.e., MainActivity) should set these to screen dimensions to ensure Bitmap scaling
    public static int maxW = 0;
    public static int maxH = 0;
    public static Bitmap defaultThumbnailBitmap;
    public static Bitmap errorImageBitmap;
    protected static int thumbH = 135;
    protected static int thumbW = 135;
    private LruCache<String, Bitmap> mMemoryCache;

    private BitmapCache()
    {
        if( this.cacheSize == 0 )
        {
            Log.d("InBitMapConstructer", "Yikes, BitmapCache.cacheSize not set!");
            return;
        }

        if( maxW == 0 || maxH == 0 )
        {
            Log.d("InBitMapConstructer", "Yikes, set maxW and maxH to get bitmap scaling.");
        }

        mMemoryCache = new LruCache<String, Bitmap>(this.cacheSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        this.defaultThumbnailBitmap = makeDefaultThumbnail();
        this.errorImageBitmap = makeErrorImage();
    }

    private static class BitmapCacheHolder {
        public static BitmapCache helper = new BitmapCache();
    }

    public static BitmapCache getInstance() {
        return BitmapCacheHolder.helper;
    }

    static protected Bitmap makeErrorImage()
    {
        int h = thumbH;
        int w = thumbW;
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bm);
        Paint darkPaint = new Paint();
        darkPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, w, h, darkPaint);
        return bm;
    }

    static protected Bitmap makeDefaultThumbnail()
    {
        int h = maxH/6;
        int w = (int)(h * 1.75);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bm);

        Paint darkPaint = new Paint();
        darkPaint.setColor(Color.rgb(50, 50, 50));

        Paint lightPaint = new Paint();
        lightPaint.setColor(Color.rgb(200, 200, 200));

        canvas.drawRect(0, 0, w, h , darkPaint);
        canvas.drawRect(5, 5, w-5,h-5,lightPaint);

        return bm;
    }

    // Thanks to https://stackoverflow.com/questions/15440647/scaled-bitmap-maintaining-aspect-ratio
    protected static Bitmap scaleBitmapAndKeepRatio(Bitmap bitmap)
    {
        Bitmap resizedBitmap = null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


       // System.out.println("before rescaling picture width is " + width + " picture height is " + height);

        float multFactor = (float) width / (float) height;

        height = maxH/6 ;

        width = (int) (height * multFactor);

       // System.out.println("then picture width is " + width + " picture height is " + height);

        resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return resizedBitmap;
    }

    public synchronized void setBitmap(String key, Bitmap bitmap)
    {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);

        int totalPSS = memoryInfo.getTotalPss() / 1024;

        String memMessage = String.format("App Memory: cache size=%d MB\nPss=%.2f MB\nPrivate=%.2f MB\nShared=%.2f MB",
                cacheSize,
                memoryInfo.getTotalPss() / 1024.0,
                memoryInfo.getTotalPrivateDirty() / 1024.0,
                memoryInfo.getTotalSharedDirty() / 1024.0);

       // Log.i("log_tag", memMessage);

        if (cacheSize < totalPSS)
        {
            Log.i("TooMuchMemory", "too much memory is being used!!!");
        }

        // add bitmap to mMemoryCache if its not already there
        if (mMemoryCache.get(key) == null)
        {
            bitmap = scaleBitmapAndKeepRatio(bitmap);

            mMemoryCache.put(key, bitmap);
            bitmap = mMemoryCache.get(key);

            if( bitmap == null )
            {
                Log.i("BitMapNull", "bit map is null after setting!");
                mMemoryCache.put(key, errorImageBitmap);
            }
        }
        else
        {
            Log.i("IdenticalKeys", "2 identical keys problem in bitmap cache!!");
        }
    }

    public synchronized Bitmap getBitmap(String key)
    {
        Bitmap bm = mMemoryCache.get(key);

        if( bm == null )
        {
            Log.d("InGetbBitmap", "BMCache miss!!! " + key);
        }
        else
        {
            //Log.d("InGetBitmap", "BMCache hit " + key );
        }
        return bm;
    }
}
