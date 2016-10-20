package com.inspired.ppc.model;

import java.util.List;

/**
 * An implementation of {@link AbstractAlbum} used to represent photos of me album;
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class PhotosOfMeAlbum extends AbstractAlbum {
    public final List<Photo> photos;

    public PhotosOfMeAlbum(List<Photo> photos) {
        super(ALBUM_PHOTOS_OF_ME, "Photos of Me");
        this.photos = photos;
    }

    @Override
    public String getCoverPhotoUrl() {
        return (photos != null && photos.size() > 0) ? photos.get(0).picture : null;
    }

    @Override
    public int getPhotosCount() {
        return photos != null ? photos.size() : 0;
    }
}
