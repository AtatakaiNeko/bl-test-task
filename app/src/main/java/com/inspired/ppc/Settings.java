package com.inspired.ppc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A wrapper around {@link android.content.SharedPreferences}.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public class Settings {

    private static final String PREF_NAME = "cool_prefs";

    private static final String PREF_PROFILE_URL = "profile_url";

    private SharedPreferences mSharedPreferences;

    public Settings(Context context) {
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Stores specified url as a link to an image which should be used as profile picture.
     *
     * @param url An url to an image.
     */
    public void setProfileUrl(String url) {
        edit().putString(PREF_PROFILE_URL, url).apply();
    }

    /**
     * @return An url for a profile picture if was set previously. {@code NULL} otherwise.
     */
    public String getProfileUrl() {
        return mSharedPreferences.getString(PREF_PROFILE_URL, null);
    }

    private SharedPreferences.Editor edit() {
        return mSharedPreferences.edit();
    }
}
