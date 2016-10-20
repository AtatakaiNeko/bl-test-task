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
    private static Settings sSettings;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Facebook SDK
        FacebookSdk.sdkInitialize(this);

        /*
        A remark:
        I could have used Dagger to inject both Picasso and Settings instances right into Views or Presenter.
        Running out of time in this case.
         */

        //Create Picasso
        sPicasso = Picasso.with(this);

        //Create settings
        sSettings = new Settings(this);
    }


    /**
     * @return A {@link Picasso} instance ready to fetch images.
     */
    public static Picasso getPicasso() {
        return sPicasso;
    }

    /**
     * @return A {@link Settings} instance.
     */
    public static Settings getSettings() {
        return sSettings;
    }
}
