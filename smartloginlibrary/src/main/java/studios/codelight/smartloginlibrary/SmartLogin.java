package studios.codelight.smartloginlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.facebook.CallbackManager;

import studios.codelight.smartloginlibrary.login.FacebookLogin;
import studios.codelight.smartloginlibrary.manager.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.DialogUtil;

/**
 * Copyright (c) 2016 Kalyan Dechiraju
 * Created by kalyandechiraju on 06/06/16.
 */
public class SmartLogin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Removed as it will be auto initialized and this method is deprecated
        // FacebookSdk.sdkInitialize(this);
    }

    public void loginWithFacebook(Activity activity, SmartLoginConfig config) {
        //Facebook login callback
        CallbackManager callbackManager = CallbackManager.Factory.create();
        new FacebookLogin().with(callbackManager).handleLoginRequest(activity, config);
    }

    public void finishLogin(Activity activity, SmartUser smartUser) {
        if (smartUser != null) {
            UserSessionManager sessionManager = new UserSessionManager();
            if (sessionManager.setUserSession(this, smartUser)) {
                Intent intent = new Intent();
                intent.putExtra(SmartLoginConfig.USER, smartUser);
                if (smartUser instanceof SmartFacebookUser) {
                    activity.setResult(SmartLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
                } else if (smartUser instanceof SmartGoogleUser) {
                    activity.setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
                } else {
                    activity.setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST, intent);
                }
                activity.finish();
            } else {
                DialogUtil.getErrorDialog(R.string.network_error, this).show();
                activity.finish();
            }
        } else {
            DialogUtil.getErrorDialog(R.string.login_failed, this).show();
            //finish();
        }
    }
}
