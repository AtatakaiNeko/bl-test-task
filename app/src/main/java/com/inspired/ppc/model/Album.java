package com.inspired.ppc.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class Album extends AbstractAlbum {

    @SerializedName("picture")
    public final Picture coverPhoto;
    @SerializedName("count")
    public final int photosCount;

    public Album(String albumId, String name, Picture coverPhoto, int photosCount) {
        super(albumId, name);

        this.coverPhoto = coverPhoto;
        this.photosCount = photosCount;
    }


    protected Album(Parcel in) {
        super(in);
        this.photosCount = in.readInt();
        //We do not need cover photo here
        this.coverPhoto = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.photosCount);
    }

    @Override
    public String getCoverPhotoUrl() {
        return coverPhoto != null ? coverPhoto.data.url : null;
    }

    @Override
    public int getPhotosCount() {
        return photosCount;
    }

    public static class Picture {
        public final Data data;

        public Picture(Data data) {
            this.data = data;
        }
    }

    public static class Data {
        public final String url;

        public Data(String url) {
            this.url = url;
        }
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
