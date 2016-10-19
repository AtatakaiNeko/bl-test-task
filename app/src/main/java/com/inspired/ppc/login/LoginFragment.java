package com.inspired.ppc.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.inspired.ppc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A a {@link Fragment} used to handle login.
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class LoginFragment extends Fragment implements FacebookCallback<LoginResult> {

    @BindView(R.id.btn_login)
    LoginButton mLoginButton;

    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, v);

        mLoginButton.setFragment(this);
        mLoginButton.registerCallback(mCallbackManager, this);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d("TAG", "Success");
    }

    @Override
    public void onCancel() {
        Log.d("TAG", "Cancel");
    }

    @Override
    public void onError(FacebookException error) {
        Log.d("TAG", "Error");
    }
}
