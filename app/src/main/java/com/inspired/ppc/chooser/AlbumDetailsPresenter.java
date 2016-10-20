package com.inspired.ppc.chooser;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspired.ppc.model.AbstractAlbum;
import com.inspired.ppc.model.Photo;
import com.inspired.ppc.model.PhotosOfMeAlbum;

import org.json.JSONException;

import java.util.List;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * A presenter used to display photos in specified album.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class AlbumDetailsPresenter extends RxPresenter<AlbumDetailsFragment> {
    private static final int REQUEST_PHOTOS = 1;
    private static final String STATE_ALBUM = "album";

    private Gson mGson = new Gson();

    private AbstractAlbum mAlbum;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(REQUEST_PHOTOS, new Func0<Observable<List<Photo>>>() {
                    @Override
                    public Observable<List<Photo>> call() {
                        if (mAlbum.isPhotosOfMeAlbum()) {
                            return Observable.just(((PhotosOfMeAlbum) mAlbum).photos)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                        } else {
                            return getPhotosObservable();
                        }
                    }
                },
                new Action2<AlbumDetailsFragment, List<Photo>>() {
                    @Override
                    public void call(AlbumDetailsFragment albumDetailsFragment, List<Photo> photos) {
                        albumDetailsFragment.onItemsNext(photos);
                    }
                },
                new Action2<AlbumDetailsFragment, Throwable>() {
                    @Override
                    public void call(AlbumDetailsFragment albumDetailsFragment, Throwable throwable) {
                        albumDetailsFragment.onError(throwable);
                    }
                });

        if (savedState != null) {
            mAlbum = savedState.getParcelable(STATE_ALBUM);
            start(REQUEST_PHOTOS);
        }
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        state.putParcelable(STATE_ALBUM, mAlbum);
    }

    private Observable<List<Photo>> getPhotosObservable() {
        return Observable.create(new Observable.OnSubscribe<List<Photo>>() {
            @Override
            public void call(Subscriber<? super List<Photo>> subscriber) {
                subscriber.onStart();
                try {
                    GraphRequest request = GraphRequest.newGraphPathRequest(
                            AccessToken.getCurrentAccessToken(),
                            mAlbum.albumId,
                            null);

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "photos{picture}");
                    request.setParameters(parameters);

                    subscriber.onNext(getPhotos(request.executeAndWait()));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Photo> getPhotos(GraphResponse response) throws JSONException {
        if (response.getError() != null) {
            throw new RuntimeException("Failed to fetch photos!");
        } else {
            List<Photo> photos;

            photos = mGson.fromJson(response.getJSONObject().getJSONObject("photos").getJSONArray("data").toString(), new TypeToken<List<Photo>>() {
            }.getType());

            GraphRequest request = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
            if (request != null) {
                photos.addAll(getPhotos(request.executeAndWait()));
            }

            return photos;
        }
    }

    public void requestAlbum(AbstractAlbum album) {
        mAlbum = album;
        start(REQUEST_PHOTOS);
    }
}
