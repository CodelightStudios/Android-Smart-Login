package studios.codelight.smartloginlibrary.login;

import android.app.Activity;

import studios.codelight.smartloginlibrary.SmartLoginConfig;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 25/09/16.
 */

interface BaseLogin {
    void handleLoginRequest(Activity activity, SmartLoginConfig config);
}
