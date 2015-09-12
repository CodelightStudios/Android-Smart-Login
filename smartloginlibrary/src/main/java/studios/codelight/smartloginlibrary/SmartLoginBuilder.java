package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginBuilder {

    private Context context;
    private SmartLoginConfig config;

    public SmartLoginBuilder() {
        config = new SmartLoginConfig();
        config.setIsFacebookEnabled(false);
        config.setIsTwitterEnabled(false);
        config.setIsGoogleEnabled(false);
        config.setCustomLoginEnabled(false);
    }

    public SmartLoginBuilder with(Context context){
        this.context = context;
        return this;
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
