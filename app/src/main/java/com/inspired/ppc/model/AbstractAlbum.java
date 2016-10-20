package com.inspired.ppc.model;

/**
 * An abstract class for an album.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public abstract class AbstractAlbum {

    public static final String ALBUM_PHOTOS_OF_ME = "photos_of_me";

    public final String albumId;
    public final String name;

    public AbstractAlbum(String albumId, String name) {
        this.albumId = albumId;
        this.name = name;
    }

    public abstract String getCoverPhotoUrl();

    public abstract int getPhotosCount();
}
