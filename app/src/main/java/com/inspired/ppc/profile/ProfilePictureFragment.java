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
import com.inspired.ppc.chooser.ProfilePictureChooseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A {@link Fragment} to display an user's profile picture.
 * <p>
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class ProfilePictureFragment extends Fragment {

    private static final int REQUEST_CODE = 2222;

    @BindView(R.id.img_profile_picture)
    CircleImageView mProfilePicture;

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

        int imgSize = Util.dpToPx(200, getResources());

        App.getPicasso()
                .load(Profile.getCurrentProfile().getProfilePictureUri(imgSize, imgSize))
                .noFade()
                .into(mProfilePicture);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    public void onPause() {
        super.onPause();
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
        return true;
    }

    @OnClick(R.id.img_profile_picture)
    public void onProfilePictureClick(View v) {
        //Start chooser
        Intent i = new Intent(getContext(), ProfilePictureChooseActivity.class);

        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Process picture selection
    }

    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            // On each update of the spring value, we adjust the scale of the image view to match the
            // springs new value. We use the SpringUtil linear interpolation function mapValueFromRangeToRange
            // to translate the spring's 0 to 1 scale to a 100% to 50% scale range and apply that to the View
            // with setScaleX/Y. Note that rendering is an implementation detail of the application and not
            // Rebound itself. If you need Gingerbread compatibility consider using NineOldAndroids to update
            // your view properties in a backwards compatible manner.
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
            mProfilePicture.setScaleX(mappedValue);
            mProfilePicture.setScaleY(mappedValue);
        }
    }
}
