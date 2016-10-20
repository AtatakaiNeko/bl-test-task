package com.inspired.ppc.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class Photo {

    @SerializedName("id")
    public final String photoId;
    @SerializedName("picture")
    public final String picture;

    public Photo(String photoId, String picture) {
        this.photoId = photoId;
        this.picture = picture;
    }
}
