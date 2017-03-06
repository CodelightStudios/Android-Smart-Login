package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginBuilder {

    private Context context;
    private SmartLoginConfig config;
    //private static final String CONFIGDATA = "config";

    public SmartLoginBuilder() {
        config = new SmartLoginConfig();
        config.setIsFacebookEnabled(false);
        config.setIsGoogleEnabled(false);
    }

    public SmartLoginBuilder with(Context context){
        this.context = context;
        return this;
    }

    public SmartLoginBuilder isFacebookLoginEnabled(boolean facebookLogin){
        config.setIsFacebookEnabled(facebookLogin);
        return this;
    }

    public SmartLoginBuilder isGoogleLoginEnabled(boolean googleLogin){
        config.setIsGoogleEnabled(googleLogin);
        return this;
    }

    public SmartLoginBuilder setSmartCustomLoginHelper(SmartCustomLoginListener mSmartCustomLoginListener) {
        config.setLoginListener(mSmartCustomLoginListener);
        return this;
    }

    public SmartLoginBuilder withFacebookAppId(String appId){
        config.setFacebookAppId(appId);
        return this;
    }

    public SmartLoginBuilder withFacebookPermissions(ArrayList<String> permissions){
        config.setFacebookPermissions(permissions);
        return this;
    }

    public SmartLoginBuilder isCustomLoginEnabled(boolean customlogin, SmartLoginConfig.LoginType loginType){
        config.setIsCustomLoginEnabled(customlogin);
        config.setLoginType(loginType);
        return this;
    }

    public SmartLoginBuilder setGoogleAppServerClientId(String clientId) {
        config.setGoogleAppServerClientId(clientId);
        return this;
    }

    public Intent build(){
        Intent intent = new Intent(context, SmartLoginActivity.class);
        intent.putExtras(config.pack());
        return intent;
    }

}
