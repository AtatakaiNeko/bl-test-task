package com.inspired.ppc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * An abstract class for an album.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public abstract class AbstractAlbum implements Parcelable {

    public static final String ALBUM_PHOTOS_OF_ME_ID = "photos_of_me";

    @SerializedName("id")
    public final String albumId;
    public final String name;

    public AbstractAlbum(String albumId, String name) {
        this.albumId = albumId;
        this.name = name;
    }

    protected AbstractAlbum(Parcel in) {
        this.albumId = in.readString();
        this.name = in.readString();
    }

    public abstract String getCoverPhotoUrl();

    public abstract int getPhotosCount();

    public boolean isPhotosOfMeAlbum() {
        return albumId != null && albumId.equalsIgnoreCase(ALBUM_PHOTOS_OF_ME_ID);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(albumId);
        parcel.writeString(name);
    }
}
