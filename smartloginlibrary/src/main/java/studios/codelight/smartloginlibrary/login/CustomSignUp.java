package studios.codelight.smartloginlibrary.login;

import android.app.Activity;

import studios.codelight.smartloginlibrary.SmartLoginConfig;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 26/09/16.
 */

public class CustomSignUp implements BaseLogin {

    @Override
    public void handleLoginRequest(Activity activity, SmartLoginConfig config) {
        config.getLoginListener().customSignup();
    }
}