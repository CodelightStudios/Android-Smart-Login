package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginBuilder {

    private Context context;
    private SmartLoginConfig config;
    //private static final String CONFIGDATA = "config";

    public SmartLoginBuilder() {
        config = new SmartLoginConfig();
        config.setAppLogo(0);
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

    public SmartLoginBuilder setCustomLoginHelper(SmartCustomLoginHelper loginHelper){
        config.setLoginHelper(loginHelper);
        return this;
    }

    public Intent build(){
        Intent intent = new Intent(context, SmartLoginActivity.class);
        //intent.putExtra(context.getString(R.string.config_data), config);
        intent.putExtra(context.getString(R.string.config_data), config.pack());
        return intent;
    }

}
