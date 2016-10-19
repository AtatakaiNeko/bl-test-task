package com.inspired.ppc;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * An {@link Application} instance.
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Facebook SDK
        FacebookSdk.sdkInitialize(this);
    }
}
