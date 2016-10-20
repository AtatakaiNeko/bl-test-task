package com.inspired.ppc.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Profile;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.facebook.rebound.ui.Util;
import com.inspired.ppc.App;
import com.inspired.ppc.R;
import com.inspired.ppc.chooser.ProfilePictureChooserActivity;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import de.hdodenhof.circleimageview.CircleImageView;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

/**
 * A {@link Fragment} to display an user's profile picture.
 * <p>
 * Created by mykhailo.zvonov on 19/10/2016.
 */

@RequiresPresenter(ProfilePicturePresenter.class)
public class ProfilePictureFragment extends NucleusSupportFragment<ProfilePicturePresenter> {

    public static final int REQUEST_CODE = 2222;

    @BindView(R.id.img_profile_picture)
    CircleImageView mProfilePicture;

    @BindView(R.id.view_ripple)
    RippleBackground mRippleBackground;

    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();

    private Spring mScaleSpring;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create spring object
        mScaleSpring = mSpringSystem.createSpring();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_picture, container, false);

        ButterKnife.bind(this, v);

        getActivity().setTitle(Profile.getCurrentProfile().getName());
        //Get image
        int imgSize = Util.dpToPx(200, getResources());
        if (savedInstanceState == null) {
            getPresenter().getProfilePicture(imgSize);
        }
        return v;
    }

    public void setProfilePicture(String url) {
        App.getPicasso()
                .load(url)
                .noFade()
                .into(mProfilePicture);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Restore view state
        mProfilePicture.setScaleX(1);
        mProfilePicture.setScaleY(1);
        mRippleBackground.startRippleAnimation();
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRippleBackground.stopRippleAnimation();
        mScaleSpring.removeListener(mSpringListener);
    }

    @OnTouch(R.id.img_profile_picture)
    public boolean onProfilePictureTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Start spring
                mScaleSpring.setEndValue(1);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //Stop spring
                mScaleSpring.setEndValue(0);
                break;
        }
        return false;
    }

    @OnClick(R.id.img_profile_picture)
    public void onProfilePictureClick(View v) {
        //Start chooser
        Intent i = new Intent(getContext(), ProfilePictureChooserActivity.class);

        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Process picture selection
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
            mProfilePicture.setScaleX(mappedValue);
            mProfilePicture.setScaleY(mappedValue);
        }
    }
}
