package studios.codelight.smartloginlibrary;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Copyright (c) 2016 Kalyan Dechiraju
 * Created by kalyandechiraju on 06/06/16.
 */
public class SmartLogin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
    }
}
