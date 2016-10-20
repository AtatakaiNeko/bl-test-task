package com.inspired.ppc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class Photo implements Parcelable {

    @SerializedName("id")
    public final String photoId;
    @SerializedName("picture")
    public final String picture;

    public Photo(String photoId, String picture) {
        this.photoId = photoId;
        this.picture = picture;
    }

    protected Photo(Parcel in) {
        this.photoId = in.readString();
        this.picture = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photoId);
        dest.writeString(this.picture);
    }


    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
