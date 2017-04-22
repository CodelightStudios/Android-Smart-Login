package studios.codelight.smartloginlibrary.util;

/**
 * Copyright (c) 2017 Codelight Studios
 * Created by kalyandechiraju on 22/04/17.
 */

public class Constants {
    public static final String LAYOUT_ID = "login_layout_id";
    public static final int FATAL = 1001;
    public static final String LOGIN_THEME = "login_page_theme";
    private static final String LOGIN_LISTENER = "login_listener_instance";
    public static final String GOOGLE_PROGRESS_DIALOG_MESSAGE = "Getting User Info…";

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

    public static final int FACEBOOK_LOGIN_REQUEST = 321;
    public static final int GOOGLE_LOGIN_REQUEST = 322;
    public static final int CUSTOM_LOGIN_REQUEST = 323;
    public static final int CUSTOM_SIGNUP_REQUEST = 324;
    public static final int LOGIN_REQUEST = 5;

    public static final String USER_SESSION = "user_session_key";
    public static final String USER_PREFS = "codelight_studios_user_prefs";
    public static final String DEFAULT_SESSION_VALUE = "No logged in user";

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
}
