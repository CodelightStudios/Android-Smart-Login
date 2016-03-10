package studios.codelight.smartloginlibrary;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginConfig{
    private int appLogo;
    private boolean isCustomLoginEnabled;
    private boolean isFacebookEnabled;
    private boolean isGoogleEnabled;
    private String facebookAppId;
    private ArrayList<String> facebookPermissions;
    private LoginType loginType;

    public static final String APPLOGO = "studios.codelight.applogo";
    public static final String USER = "studios.codelight.user";
    public static final String CUSTOMLOGINFLAG = "studios.codelight.custom_login_flag";
    public static final String FACEBOOKFLAG = "studios.codelight.facebook_flag";
    public static final String FACEBOOKPERMISSIONS = "studios.codelight.facebook_permissions";
    //public static final String TWITTERFLAG = "studios.codelight.twitter_flag";
    public static final String GOOGLEFLAG = "studios.codelight.google_flag";
    public static final String FACEBOOKID = "studios.codelight.facebook_id";
    public static final String CUSTOMUSERFLAG = "studios.codelight.custom_user";
    public static final String CUSTOMLOGINTYPE = "studios.codelight.custom_login_type";

    public static final String USER_TYPE = "user_type";

    public static final int FACEBOOK_LOGIN_REQUEST = 1;
    public static final int GOOGLE_LOGIN_REQUEST = 2;
    public static final int CUSTOM_LOGIN_REQUEST = 3;
    public static final int CUSTOM_SIGNUP_REQUEST = 4;
    public static final int LOGIN_REQUEST = 5;

    public SmartLoginConfig() {
    }

    public int getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(int appLogo) {
        this.appLogo = appLogo;
    }

    public boolean isCustomLoginEnabled() {
        return isCustomLoginEnabled;
    }

    public void setIsCustomLoginEnabled(boolean isCustomLoginEnabled) {
        this.isCustomLoginEnabled = isCustomLoginEnabled;
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

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public ArrayList<String> getFacebookPermissions() {
        return facebookPermissions;
    }

    public void setFacebookPermissions(ArrayList<String> facebookPermissions) {
        this.facebookPermissions = facebookPermissions;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public static ArrayList<String> getDefaultFacebookPermissions(){
        ArrayList<String> defaultPermissions = new ArrayList<>();
        defaultPermissions.add("public_profile");
        defaultPermissions.add("email");
        defaultPermissions.add("user_birthday");
        return defaultPermissions;
    }

    public Bundle pack(){
        Bundle bundle = new Bundle();
        if(appLogo != 0) {
            bundle.putInt(APPLOGO, appLogo);
        }
        bundle.putBoolean(FACEBOOKFLAG, isFacebookEnabled);
        bundle.putBoolean(GOOGLEFLAG, isGoogleEnabled);
        bundle.putString(FACEBOOKID, facebookAppId);
        bundle.putStringArrayList(FACEBOOKPERMISSIONS, facebookPermissions);
        bundle.putBoolean(CUSTOMLOGINFLAG, isCustomLoginEnabled);
        if(loginType != null) {
            bundle.putSerializable(CUSTOMLOGINTYPE, loginType);
        }
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
        if(keys.contains(FACEBOOKID)){
            loginConfig.setFacebookAppId(bundle.getString(FACEBOOKID));
        }
        if (keys.contains(FACEBOOKPERMISSIONS)){
            loginConfig.setFacebookPermissions(bundle.getStringArrayList(FACEBOOKPERMISSIONS));
        }
        if (keys.contains(CUSTOMLOGINFLAG)){
            loginConfig.setIsCustomLoginEnabled(bundle.getBoolean(CUSTOMLOGINFLAG));
        }

        if(keys.contains(CUSTOMLOGINTYPE)) {
            loginConfig.setLoginType((LoginType) bundle.getSerializable(CUSTOMLOGINTYPE));
        } else {
            loginConfig.setLoginType(LoginType.withEmail);
        }

        return loginConfig;
    }

    public static class FacebookFields{
        public static final String EMAIL = "email";
        public static final String ID = "id";
        public static final String BIRTHDAY = "birthday";
        public static final String GENDER = "gender";
        public static final String FIRST_NAME = "first_name";
        public static final String MIDDLE_NAME = "middle_name";
        public static final String LAST_NAME = "last_name";
        public static final String NAME = "name";
        public static final String LINK = "link";
    }

    public enum Gender{
        male, female
    }

    public enum LoginType {
        withEmail,
        withUsername
    }

}
