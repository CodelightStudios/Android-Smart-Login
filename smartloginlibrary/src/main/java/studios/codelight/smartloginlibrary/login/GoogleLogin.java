package studios.codelight.smartloginlibrary.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import studios.codelight.smartloginlibrary.R;
import studios.codelight.smartloginlibrary.SmartLoginActivity;
import studios.codelight.smartloginlibrary.SmartLoginConfig;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 26/09/16.
 */

public class GoogleLogin implements BaseLogin {
    private GoogleApiClient mGoogleApiClient;

    public GoogleLogin with(GoogleApiClient googleApiClient) {
        this.mGoogleApiClient = googleApiClient;
        return this;
    }

    @Override
    public void handleLoginRequest(Activity activity, SmartLoginConfig config) {
        if (!mGoogleApiClient.isConnecting()) {
            ProgressDialog progress = ProgressDialog.show(activity, "", activity.getString(R.string.logging_holder), true);
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            activity.startActivityForResult(signInIntent, SmartLoginActivity.GOOGLE_SIGN_IN_REQUEST);
            progress.dismiss();
        }
    }
}
