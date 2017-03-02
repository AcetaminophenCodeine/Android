package edu.utexas.cs371m.rs47345.redfetch;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;

public class RedditRecord implements Serializable
{
    public String title;
    public URL thumbnailURL;
    public URL imageURL;


}
