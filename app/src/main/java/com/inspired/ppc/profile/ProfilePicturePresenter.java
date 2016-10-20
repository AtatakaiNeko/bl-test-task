package com.inspired.ppc.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.Profile;
import com.inspired.ppc.App;
import com.inspired.ppc.chooser.ProfilePictureChooserActivity;
import com.inspired.ppc.model.Photo;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * A presenter for the {@link ProfilePictureFragment}.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class ProfilePicturePresenter extends RxPresenter<ProfilePictureFragment> {
    private static final int GET_PROFILE_PICTURE = 1;
    private static final String STATE_SIZE = "size";

    private int mPictureSize;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        if (savedState != null) {
            mPictureSize = savedState.getInt(STATE_SIZE);
        }
        restartableLatestCache(GET_PROFILE_PICTURE, new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String profileUrl = App.getSettings().getProfileUrl();
                if (TextUtils.isEmpty(profileUrl)) {
                    profileUrl = Profile.getCurrentProfile().getProfilePictureUri(mPictureSize, mPictureSize).toString();
                }
                return Observable.just(profileUrl)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<ProfilePictureFragment, String>() {
            @Override
            public void call(ProfilePictureFragment profilePictureFragment, String s) {
                profilePictureFragment.setProfilePicture(s);
            }
        });
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        state.putInt(STATE_SIZE, mPictureSize);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ProfilePictureFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Photo img = data.getParcelableExtra(ProfilePictureChooserActivity.EXTRA_PHOTO);
            App.getSettings().setProfileUrl(img.getImageUrlFor(mPictureSize));
            start(GET_PROFILE_PICTURE);
        }
    }

    public void getProfilePicture(int size) {
        mPictureSize = size;
        start(GET_PROFILE_PICTURE);
    }
}
