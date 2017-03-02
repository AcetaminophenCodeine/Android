package com.example.kierra.klru;

/**
 * Created by Hatter on 11/28/16.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.LruCache;



/*BitmapCache.java


One subtlety here is that the BitmapCache is a singleton, but it doesn’t know how much
memory your app has, only the main activity knows that. So we have a public static int called
cacheSize that the MainActivity writes before anyone uses the BitmapCache. We give you this
code. We also provide code to set maxH and maxW to the screen height and width. By doing this,
the code in BitmapCache, when it retrieves an image, will resize it for the screen to save memory.
What I learned about Bitmaps in doing this project is that they are large. They take up a
lot of memory. You fetch possibly large images for this project. Turning a large jpeg file
into a bitmap, then displaying it on a tiny Android screen is a silly thing to do, so we do
this preemptive resizing.

Images can be so large that even converting them causes the code to throw an out of memory
exception. So we have a default error image (which could no doubt more informative) that we
insert in our cache if we fail to convert an image we download. Just failing to convert
would frustrate your client code down the road (I mean, your client wanted to fetch an
image, and now it isn’t in the global cache? What gives?)

So the code that inserts an item tests to see if the insert was successful and if not it inserts
a “placeholder” image.

Check out how this class builds errorImageBitmap and defaultThumbnailBitmap in the constructor.
This class has a lot of defensive programing and some useful programming tricks.

*/

public class BitmapCache
{
    // We assume main activity will set this before getInstance() is called
    public static int cacheSize = 0;
    // Clients (i.e., MainActivity) should set these to screen dimensions to ensure Bitmap scaling
    public static int maxW;
    public static int maxH;
    public static Bitmap defaultThumbnailBitmap;
    public static Bitmap errorImageBitmap;
    protected static int thumbH = 135;
    protected static int thumbW = 135;
    private LruCache<String, Bitmap> mMemoryCache;

    private BitmapCache()
    {
        if( this.cacheSize == 0 )
        {
            Log.d("InConstructer", "Yikes, BitmapCache.cacheSize not set!");
            return;
        }

        if( maxW == 0 || maxH == 0 )
        {
            Log.d("InConstructer", "Yikes, set maxW and maxH to get bitmap scaling.");
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
        int h = thumbH;
        int w = thumbW;
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

    // Thanks to https://stackoverflow.com/questions/15440647/scaled-bitmap-maintaining-aspect-ratio
    protected static Bitmap scaleBitmapAndKeepRatio(Bitmap bitmap)
    {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;

        if(originalHeight > originalWidth)
        {
            newHeight = maxH;
            multFactor = (float) originalWidth/(float) originalHeight;
            newWidth = (int) (newHeight*multFactor);
        }
        else if(originalWidth > originalHeight)
        {
            newWidth = maxW;
            multFactor = (float) originalHeight/ (float)originalWidth;
            newHeight = (int) (newWidth*multFactor);
        }
        else
        {
            // originalHeight==originalWidth
            newHeight = maxH;
            newWidth = maxW;
        }

        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    public synchronized void setBitmap(String key, Bitmap bitmap)
    {
        // add bitmap to mMemoryCache if its not already there
        if (mMemoryCache.get(key) == null)
        {
            // Only scale if we have to

            mMemoryCache.put(key, bitmap);
            bitmap = mMemoryCache.get(key);

            if( bitmap == null )
            {
                mMemoryCache.put(key, errorImageBitmap);
            }
        }
    }

    public synchronized Bitmap getBitmap(String key)
    {
        Bitmap bm = mMemoryCache.get(key);

        if( bm == null )
        {
            Log.d("InGetbBitmap", "BMCache miss "+key);
        }
        else
        {
            Log.d("InGetBitmap", "BMCache hit " + key );
        }
        return bm;
    }
}
