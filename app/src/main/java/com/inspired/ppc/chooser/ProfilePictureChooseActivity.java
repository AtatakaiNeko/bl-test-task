package com.inspired.ppc.chooser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * An {@link AppCompatActivity} to help user to choose a profile picture.
 * <p>
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class ProfilePictureChooseActivity extends AppCompatActivity {

    public static final String EXTRA_IMG_ID = "image_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new AlbumsFragment())
                    .commit();
        }
    }
}
