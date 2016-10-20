package com.inspired.ppc.model;

import android.os.Parcel;

import java.util.List;

/**
 * An implementation of {@link AbstractAlbum} used to represent photos of me album;
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class PhotosOfMeAlbum extends AbstractAlbum {
    public final List<Photo> photos;

    public PhotosOfMeAlbum(List<Photo> photos) {
        super(ALBUM_PHOTOS_OF_ME_ID, "Photos of Me");
        this.photos = photos;
    }

    protected PhotosOfMeAlbum(Parcel in) {
        super(in);
        this.photos = in.createTypedArrayList(Photo.CREATOR);
    }

    @Override
    public String getCoverPhotoUrl() {
        return (photos != null && photos.size() > 0) ? photos.get(0).picture : null;
    }

    @Override
    public int getPhotosCount() {
        return photos != null ? photos.size() : 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.photos);
    }


    public static final Creator<PhotosOfMeAlbum> CREATOR = new Creator<PhotosOfMeAlbum>() {
        @Override
        public PhotosOfMeAlbum createFromParcel(Parcel source) {
            return new PhotosOfMeAlbum(source);
        }

        @Override
        public PhotosOfMeAlbum[] newArray(int size) {
            return new PhotosOfMeAlbum[size];
        }
    };
}
