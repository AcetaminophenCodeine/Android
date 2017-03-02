package com.example.kierra.klru;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hatter on 11/15/16.
 */

public class Album implements Parcelable {

        public String name;
        public URL url;
        public URL thumbnail;
        public ArrayList<URL> images;

        public Album() {

           // this.name = name;
           // this.url = url;
           // this.thumbnail = thumbnail;
           // this.images = images;
        }

    protected Album(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
