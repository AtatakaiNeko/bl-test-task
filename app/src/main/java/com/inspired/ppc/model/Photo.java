package com.inspired.ppc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * An object to describe a photo model received from Facebook API.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class Photo implements Parcelable {

    @SerializedName("id")
    public final String photoId;
    @SerializedName("picture")
    public final String picture;
    public final Image[] images;

    public Photo(String photoId, String picture, Image[] images) {
        this.photoId = photoId;
        this.picture = picture;
        this.images = images;
    }

    protected Photo(Parcel in) {
        this.photoId = in.readString();
        this.picture = in.readString();
        this.images = in.createTypedArray(Image.CREATOR);
    }

    /**
     * Helps to select an image with the best quality for specified width,
     *
     * @param desiredWidth - A minimum desired width.
     * @return An url to the image.
     */
    public String getImageUrlFor(int desiredWidth) {
        if (images == null || images.length == 0) {
            return null;
        }

        if (images.length == 1) {
            return images[0].source;
        }

        //Sort by width
        Arrays.sort(images);
        for (int i = images.length - 1; i >= 0; i--) {
            if (images[i].width < desiredWidth) {
                if (i != images.length - 1) {
                    //Get previous image
                    return images[i + 1].source;
                } else {
                    return images[i].source;
                }
            }
        }
        //Otherwise return the smallest
        return images[0].source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photoId);
        dest.writeString(this.picture);
        dest.writeTypedArray(this.images, flags);
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

    public static class Image implements Parcelable, Comparable<Image> {
        public final int height;
        public final int width;
        public final String source;


        public Image(int height, int width, String source) {
            this.height = height;
            this.width = width;
            this.source = source;
        }

        protected Image(Parcel in) {
            this.height = in.readInt();
            this.width = in.readInt();
            this.source = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.height);
            dest.writeInt(this.width);
            dest.writeString(this.source);
        }


        public static final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel source) {
                return new Image(source);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        @Override
        public int compareTo(Image image) {
            return this.width == image.width ? 0 : this.width > image.width ? 1 : -1;
        }
    }

}
