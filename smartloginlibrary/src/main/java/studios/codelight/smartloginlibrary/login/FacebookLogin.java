package studios.codelight.smartloginlibrary.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.ArrayList;

import studios.codelight.smartloginlibrary.R;
import studios.codelight.smartloginlibrary.SmartLoginActivity;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.util.UserUtil;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 25/09/16.
 */

public class FacebookLogin implements BaseLogin {
    private CallbackManager callbackManager;


    public FacebookLogin with(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
        return this;
    }

    @Override
    public void handleLoginRequest(final Activity activity, SmartLoginConfig config) {
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
                        UserUtil util = new UserUtil();
                        SmartFacebookUser facebookUser = util.populateFacebookUser(jsonObject, loginResult.getAccessToken());
                        if (facebookUser != null) {
                            ((SmartLoginActivity) activity).finishLogin(facebookUser);
                        } else {
                            activity.finish();
                        }
                    }
                });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progress.dismiss();
                activity.finish();
                Log.d("Facebook Login", "User cancelled the login process");
            }

            @Override
            public void onError(FacebookException e) {
                progress.dismiss();
                activity.finish();
                Toast.makeText(activity, R.string.network_error + " " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
