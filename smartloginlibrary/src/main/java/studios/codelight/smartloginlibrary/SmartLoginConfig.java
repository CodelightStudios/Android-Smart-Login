package studios.codelight.smartloginlibrary;

import android.os.Bundle;

import java.util.Set;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginConfig{
    private int appLogo;
    private boolean isFacebookEnabled;
    private boolean isGoogleEnabled;

    public static final String APPLOGO = "studios.codelight.applogo";
    public static final String USER = "studios.codelight.user";
    public static final String FACEBOOKFLAG = "studios.codelight.facebook_flag";
    //public static final String TWITTERFLAG = "studios.codelight.twitter_flag";
    public static final String GOOGLEFLAG = "studios.codelight.google_flag";
    public static final String LOGINHELPER = "studios.codelight.loginhelper";

    public static final int FACEBOOK_LOGIN_REQUEST = 1;
    public static final int GOOGLE_LOGIN_REQUEST = 2;
    public static final int CUSTOM_LOGIN_REQUEST = 3;
    public static final int CUSTOM_SIGNUP_REQUEST = 4;

    public SmartLoginConfig() {
    }

    public int getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(int appLogo) {
        this.appLogo = appLogo;
    }

    public boolean isFacebookEnabled() {
        return isFacebookEnabled;
    }

    public void setIsFacebookEnabled(boolean isFacebookEnabled) {
        this.isFacebookEnabled = isFacebookEnabled;
    }

    public boolean isGoogleEnabled() {
        return isGoogleEnabled;
    }

    public void setIsGoogleEnabled(boolean isGoogleEnabled) {
        this.isGoogleEnabled = isGoogleEnabled;
    }

    public Bundle pack(){
        Bundle bundle = new Bundle();
        if(appLogo != 0) {
            bundle.putInt(APPLOGO, appLogo);
        }
        bundle.putBoolean(FACEBOOKFLAG, isFacebookEnabled);
        bundle.putBoolean(GOOGLEFLAG, isGoogleEnabled);
//        bundle.putSerializable(LOGINHELPER, loginHelper);
        return bundle;
    }

    public static SmartLoginConfig unpack(Bundle bundle){
        //new LoginConfig to return
        SmartLoginConfig loginConfig = new SmartLoginConfig();
        Set<String> keys = bundle.keySet();

        if(keys.contains(APPLOGO)){
            loginConfig.setAppLogo(bundle.getInt(APPLOGO));
        }
        if (keys.contains(FACEBOOKFLAG)){
            loginConfig.setIsFacebookEnabled(bundle.getBoolean(FACEBOOKFLAG));
        }
        if(keys.contains(GOOGLEFLAG)){
            loginConfig.setIsGoogleEnabled(bundle.getBoolean(GOOGLEFLAG));
        }

        return loginConfig;
    }

}
