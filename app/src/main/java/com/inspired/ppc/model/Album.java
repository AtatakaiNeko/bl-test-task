package com.inspired.ppc.model;

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
}
