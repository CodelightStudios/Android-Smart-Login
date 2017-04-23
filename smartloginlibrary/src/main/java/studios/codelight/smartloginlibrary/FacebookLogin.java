package studios.codelight.smartloginlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.ArrayList;

import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.util.Constants;
import studios.codelight.smartloginlibrary.util.SmartLoginException;
import studios.codelight.smartloginlibrary.util.UserUtil;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 25/09/16.
 */

public class FacebookLogin extends SmartLogin {

    private CallbackManager callbackManager;
    public FacebookLogin() {
        //Facebook login callback
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void login(@NonNull SmartLoginConfig config) {
        final Activity activity = config.getActivity();
        final SmartLoginCallbacks callback = config.getCallback();
        final ProgressDialog progress = ProgressDialog.show(activity, "", activity.getString(R.string.logging_holder), true);
        ArrayList<String> permissions = config.getFacebookPermissions();
        if (permissions == null) {
            permissions = SmartLoginConfig.getDefaultFacebookPermissions();
        }
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                progress.setMessage(activity.getString(R.string.getting_data));
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                        progress.dismiss();
                        SmartFacebookUser facebookUser = UserUtil.populateFacebookUser(jsonObject, loginResult.getAccessToken());
                        // Save the user
                        UserSessionManager.setUserSession(activity, facebookUser);
                        callback.onLoginSuccess(facebookUser);
                    }
                });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progress.dismiss();
                Log.d("Facebook Login", "User cancelled the login process");
                callback.onLoginFailure(new SmartLoginException("User cancelled the login request", LoginType.Facebook));
            }

            @Override
            public void onError(FacebookException e) {
                progress.dismiss();
                callback.onLoginFailure(new SmartLoginException(e.getMessage(), e, LoginType.Facebook));
            }
        });
    }

    @Override
    public void signup(SmartLoginConfig config) {

    }

    @Override
    public boolean logout(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            LoginManager.getInstance().logOut();
            editor.remove(Constants.USER_TYPE);
            editor.remove(Constants.USER_SESSION);
            editor.apply();
            return true;
        } catch (Exception e) {
            Log.e("FacebookLogin", e.getMessage());
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, SmartLoginConfig config) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
