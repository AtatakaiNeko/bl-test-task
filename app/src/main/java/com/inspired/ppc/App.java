package com.inspired.ppc;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

/**
 * An {@link Application} instance.
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class App extends Application {

    private static Picasso sPicasso;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Facebook SDK
        FacebookSdk.sdkInitialize(this);

        //Create Picasso
        sPicasso = Picasso.with(this);
    }

    /**
     * @return A {@link Picasso} instance ready to fetch images.
     */
    public static Picasso getPicasso() {
        return sPicasso;
    }
}
