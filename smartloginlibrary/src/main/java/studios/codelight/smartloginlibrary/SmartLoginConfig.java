package studios.codelight.smartloginlibrary;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Set;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginConfig {
    public static final String LAYOUT_ID = "login_layout_id";
    public static final int FATAL = 1001;
    public static final String LOGIN_THEME = "login_page_theme";
    private static final String LOGIN_LISTENER = "login_listener_instance";
    private boolean isCustomLoginEnabled;
    private boolean isFacebookEnabled;
    private boolean isGoogleEnabled;
    private String facebookAppId;
    private ArrayList<String> facebookPermissions;
    private LoginType loginType;
    private String googleAppServerClientId;
    private SmartCustomLoginListener loginListener;

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

    public static final String FACEBOOK_BUTTON_ID = "smart_facebook_login_button";
    public static final String GOOGLE_BUTTON_ID = "smart_google_login_button";
    public static final String CUSTOM_LOGIN_BUTTON_ID = "smart_custom_login_button";
    public static final String CUSTOM_SIGNUP_BUTTON_ID = "smart_custom_signup_button";
    public static final String EMAIL_EDITTEXT_TAG_ID = "smart_email_edittext";
    public static final String PASSWORD_EDITTEXT_TAG_ID = "smart_password_edittext";

    public SmartLoginConfig() {
    }

    public boolean isCustomLoginEnabled() {
        return isCustomLoginEnabled;
    }

    void setIsCustomLoginEnabled(boolean isCustomLoginEnabled) {
        this.isCustomLoginEnabled = isCustomLoginEnabled;
    }

    public boolean isFacebookEnabled() {
        return isFacebookEnabled;
    }

    void setIsFacebookEnabled(boolean isFacebookEnabled) {
        this.isFacebookEnabled = isFacebookEnabled;
    }

    public boolean isGoogleEnabled() {
        return isGoogleEnabled;
    }

    void setIsGoogleEnabled(boolean isGoogleEnabled) {
        this.isGoogleEnabled = isGoogleEnabled;
    }

    String getFacebookAppId() {
        return facebookAppId;
    }

    void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public ArrayList<String> getFacebookPermissions() {
        return facebookPermissions;
    }

    void setFacebookPermissions(ArrayList<String> facebookPermissions) {
        this.facebookPermissions = facebookPermissions;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    String getGoogleAppServerClientId() {
        return googleAppServerClientId;
    }

    void setGoogleAppServerClientId(String googleAppServerClientId) {
        this.googleAppServerClientId = googleAppServerClientId;
    }

    public SmartCustomLoginListener getLoginListener() {
        return loginListener;
    }

    public void setLoginListener(SmartCustomLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public static ArrayList<String> getDefaultFacebookPermissions() {
        ArrayList<String> defaultPermissions = new ArrayList<>();
        defaultPermissions.add("public_profile");
        defaultPermissions.add("email");
        defaultPermissions.add("user_birthday");
        return defaultPermissions;
    }

    Bundle pack() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(FACEBOOKFLAG, isFacebookEnabled);
        bundle.putBoolean(GOOGLEFLAG, isGoogleEnabled);
        bundle.putString(FACEBOOKID, facebookAppId);
        bundle.putStringArrayList(FACEBOOKPERMISSIONS, facebookPermissions);
        bundle.putBoolean(CUSTOMLOGINFLAG, isCustomLoginEnabled);
        if (loginType != null) {
            bundle.putSerializable(CUSTOMLOGINTYPE, loginType);
        }
        bundle.putSerializable(LOGIN_LISTENER, loginListener);
        return bundle;
    }

    static SmartLoginConfig unpack(Bundle bundle) {
        //new LoginConfig to return
        SmartLoginConfig loginConfig = new SmartLoginConfig();
        Set<String> keys = bundle.keySet();

        if (keys.contains(FACEBOOKFLAG)) {
            loginConfig.setIsFacebookEnabled(bundle.getBoolean(FACEBOOKFLAG));
        }
        if (keys.contains(GOOGLEFLAG)) {
            loginConfig.setIsGoogleEnabled(bundle.getBoolean(GOOGLEFLAG));
        }
        if (keys.contains(FACEBOOKID)) {
            loginConfig.setFacebookAppId(bundle.getString(FACEBOOKID));
        }
        if (keys.contains(FACEBOOKPERMISSIONS)) {
            loginConfig.setFacebookPermissions(bundle.getStringArrayList(FACEBOOKPERMISSIONS));
        }
        if (keys.contains(CUSTOMLOGINFLAG)) {
            loginConfig.setIsCustomLoginEnabled(bundle.getBoolean(CUSTOMLOGINFLAG));
        }

        if (keys.contains(CUSTOMLOGINTYPE)) {
            loginConfig.setLoginType((LoginType) bundle.getSerializable(CUSTOMLOGINTYPE));
        } else {
            loginConfig.setLoginType(LoginType.withEmail);
        }

        if (keys.contains(LOGIN_LISTENER)) {
            loginConfig.setLoginListener((SmartCustomLoginListener) bundle.getSerializable(LOGIN_LISTENER));
        }

        return loginConfig;
    }

    public static class FacebookFields {
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

    public enum Gender {
        male, female
    }

    public enum LoginType {
        withEmail,
        withUsername
    }

}
