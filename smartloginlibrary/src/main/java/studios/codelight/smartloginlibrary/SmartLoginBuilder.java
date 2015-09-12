package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginBuilder {

    private Context context;
    private SmartLoginConfig config;

    public SmartLoginBuilder(Context context) {
        this.context = context;
        config = new SmartLoginConfig();
    }

    public SmartLoginBuilder isFacebookLoginEnabled(boolean facebookLogin){
        config.setIsFacebookEnabled(facebookLogin);
        return this;
    }

    public SmartLoginBuilder isTwitterLoginEnabled(boolean twitterLogin){
        config.setIsTwitterEnabled(twitterLogin);
        return this;
    }

    public SmartLoginBuilder isGoogleLoginEnabled(boolean googleLogin){
        config.setIsTwitterEnabled(googleLogin);
        return this;
    }

    public SmartLoginBuilder setCustomLoginHelper(SmartLoginHelper loginHelper){
        config.setLoginHelper(loginHelper);
        return this;
    }

    public Intent build(){
        return new Intent(context, SmartLoginActivity.class);
    }

}
