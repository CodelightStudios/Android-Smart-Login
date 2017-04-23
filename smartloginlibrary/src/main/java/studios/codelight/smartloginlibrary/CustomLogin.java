package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.Constants;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 26/09/16.
 */

public class CustomLogin extends SmartLogin {
    @Override
    public void login(SmartLoginConfig config) {
        SmartUser user = config.getCallback().doCustomLogin();
        if (user != null) {
            // Save the user
            UserSessionManager.setUserSession(config.getActivity(), user);
            config.getCallback().onLoginSuccess(user);
        } else {
            config.getCallback().onLoginFailure(new SmartLoginException("Custom login failed", LoginType.CustomLogin));
        }
    }

    @Override
    public void signup(SmartLoginConfig config) {
        SmartUser user = config.getCallback().doCustomSignup();
        if (user != null) {
            // Save the user
            UserSessionManager.setUserSession(config.getActivity(), user);
            config.getCallback().onLoginSuccess(user);
        } else {
            config.getCallback().onLoginFailure(new SmartLoginException("Custom signup failed", LoginType.CustomLogin));
        }
    }

    @Override
    public boolean logout(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(Constants.USER_TYPE);
            editor.remove(Constants.USER_SESSION);
            editor.apply();
            return true;
        } catch (Exception e) {
            Log.e("CustomLogin", e.getMessage());
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, SmartLoginConfig config) {

    }
}
