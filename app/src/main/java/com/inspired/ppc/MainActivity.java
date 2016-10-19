package com.inspired.ppc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.inspired.ppc.login.LoginFragment;
import com.inspired.ppc.profile.ProfilePictureFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            AccessToken currentToken = AccessToken.getCurrentAccessToken();
            if (currentToken == null || currentToken.isExpired()) {
                //No token or expired
                //Go to login
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new LoginFragment())
                        .commit();
            } else {
                //Go to profile screen
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new ProfilePictureFragment())
                        .commit();
            }
        }
    }
}
