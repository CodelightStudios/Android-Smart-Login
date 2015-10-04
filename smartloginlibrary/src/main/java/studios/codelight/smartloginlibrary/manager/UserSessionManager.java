package studios.codelight.smartloginlibrary.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Created by Kalyan on 9/29/2015.
 */
public class UserSessionManager {

    static final String SESSION_KEY = "user_session_key";
    static final String USER_PREFS = "codelight_studios_user_prefs";
    static final String DEFAULT_SESSION_VALUE = "No logged in user";

    public static SmartUser getCurrentUser(Context context){
        SmartUser smartUser = null;
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String sessionUser = preferences.getString(SESSION_KEY, DEFAULT_SESSION_VALUE);
        String user_type = preferences.getString(SmartLoginConfig.USER_TYPE, SmartLoginConfig.CUSTOMUSERFLAG);
        if(!sessionUser.equals(DEFAULT_SESSION_VALUE)){
            try {
                switch (user_type) {
                    case SmartLoginConfig.FACEBOOKFLAG:
                        smartUser = gson.fromJson(sessionUser, SmartFacebookUser.class);
                        break;
                    case SmartLoginConfig.GOOGLEFLAG:
                        smartUser = gson.fromJson(sessionUser, SmartGoogleUser.class);
                        break;
                    default:
                        smartUser = gson.fromJson(sessionUser, SmartUser.class);
                        break;
                }
            }catch (Exception e){
                Log.e("GSON", e.getMessage());
            }
        }
        return smartUser;
    }

    public boolean setUserSession(Context context, SmartUser smartUser){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        try {
            preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            editor = preferences.edit();

            if (smartUser instanceof SmartFacebookUser){
                editor.putString(SmartLoginConfig.USER_TYPE, SmartLoginConfig.FACEBOOKFLAG);
            } else if(smartUser instanceof SmartGoogleUser){
                editor.putString(SmartLoginConfig.USER_TYPE, SmartLoginConfig.GOOGLEFLAG);
            } else {
                editor.putString(SmartLoginConfig.USER_TYPE, SmartLoginConfig.CUSTOMUSERFLAG);
            }

            Gson gson = new Gson();
            smartUser.setPassword(null);
            String sessionUser = gson.toJson(smartUser);
            Log.d("GSON", sessionUser);
            editor.putString(SESSION_KEY, sessionUser);
            editor.apply();
            return true;
        } catch (Exception e){
            Log.e("Session Error", e.getMessage());
            return false;
        }
    }
}
