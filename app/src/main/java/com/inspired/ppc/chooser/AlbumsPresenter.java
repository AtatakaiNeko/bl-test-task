package com.inspired.ppc.chooser;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspired.ppc.model.AbstractAlbum;
import com.inspired.ppc.model.Album;
import com.inspired.ppc.model.Photo;
import com.inspired.ppc.model.PhotosOfMeAlbum;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * A presenter for the {@link AlbumsFragment}.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class AlbumsPresenter extends RxPresenter<AlbumsFragment> {

    private static final int REQUEST_ALBUMS = 1;

    private Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_ALBUMS, new Func0<Observable<List<AbstractAlbum>>>() {
                    @Override
                    public Observable<List<AbstractAlbum>> call() {
                        return Observable.zip(getPhotoOfMeAlbumObservable(),
                                getAlbumsObservable(),
                                new Func2<PhotosOfMeAlbum, List<Album>, List<AbstractAlbum>>() {
                                    @Override
                                    public List<AbstractAlbum> call(PhotosOfMeAlbum photosOfMeAlbum, List<Album> albums) {
                                        List<AbstractAlbum> result = new ArrayList<>();
                                        if (photosOfMeAlbum != null && photosOfMeAlbum.getPhotosCount() > 0) {
                                            result.add(photosOfMeAlbum);
                                        }

                                        if (albums != null) {
                                            result.addAll(albums);
                                        }
                                        return result;
                                    }
                                }).observeOn(AndroidSchedulers.mainThread());
                    }
                },
                new Action2<AlbumsFragment, List<AbstractAlbum>>() {
                    @Override
                    public void call(AlbumsFragment albumsFragment, List<AbstractAlbum> albums) {
                        albumsFragment.onItemsNext(albums);
                    }
                },
                new Action2<AlbumsFragment, Throwable>() {
                    @Override
                    public void call(AlbumsFragment albumsFragment, Throwable throwable) {
                        albumsFragment.onItemsError(throwable);
                    }
                });
    }

    private Observable<PhotosOfMeAlbum> getPhotoOfMeAlbumObservable() {
        return Observable.create(new Observable.OnSubscribe<PhotosOfMeAlbum>() {
            @Override
            public void call(Subscriber<? super PhotosOfMeAlbum> subscriber) {
                subscriber.onStart();
                try {
                    GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                            "/me/photos",
                            null);

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "picture");
                    request.setParameters(parameters);

                    List<Photo> photosOfMe = getPhotosOfMe(request.executeAndWait());

                    subscriber.onNext(new PhotosOfMeAlbum(photosOfMe));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Photo> getPhotosOfMe(GraphResponse response) throws JSONException {
        List<Photo> photos;
        if (response.getError() != null) {
            throw new RuntimeException("Photos of me Failed.");
        } else {

            photos = mGson.fromJson(response.getJSONObject().getJSONArray("data").toString(), new TypeToken<List<Photo>>() {
            }.getType());

            GraphRequest request = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
            if (request != null) {
                photos.addAll(getPhotosOfMe(request.executeAndWait()));
            }
        }

        return photos;
    }

    private Observable<List<Album>> getAlbumsObservable() {
        return Observable.create(new Observable.OnSubscribe<List<Album>>() {
            @Override
            public void call(Subscriber<? super List<Album>> subscriber) {
                subscriber.onStart();
                try {
                    GraphRequest request = GraphRequest.newGraphPathRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/me/albums",
                            null);

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "count,picture{url},name,id");
                    request.setParameters(parameters);

                    List<Album> albums = getAlbums(request.executeAndWait());

                    subscriber.onNext(albums);
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Album> getAlbums(GraphResponse response) throws JSONException {
        List<Album> albums;

        if (response.getError() != null) {
            throw new RuntimeException("Failed to fetch albums.");
        } else {
            albums = mGson.fromJson(response.getJSONObject().getJSONArray("data").toString(), new TypeToken<List<Album>>() {
            }.getType());

            GraphRequest request = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
            if (request != null) {
                albums.addAll(getAlbums(request.executeAndWait()));
            }
        }

        return albums;
    }

    public void requestAlbums() {
        start(REQUEST_ALBUMS);
    }
}
