package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.Constants;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/29/2015.
 */
public class UserSessionManager {

    /**
        This static method can be called to get the logged in user.
        It reads from the shared preferences and builds a SmartUser object and returns it.
        If no user is logged in it returns null
    */
    public static SmartUser getCurrentUser(Context context){
        SmartUser smartUser = null;
        SharedPreferences preferences = context.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String sessionUser = preferences.getString(Constants.USER_SESSION, Constants.DEFAULT_SESSION_VALUE);
        String user_type = preferences.getString(Constants.USER_TYPE, Constants.CUSTOMUSERFLAG);
        if(!sessionUser.equals(Constants.DEFAULT_SESSION_VALUE)){
            try {
                switch (user_type) {
                    case Constants.FACEBOOKFLAG:
                        smartUser = gson.fromJson(sessionUser, SmartFacebookUser.class);
                        break;
                    case Constants.GOOGLEFLAG:
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

    /**
        This method sets the session object for the current logged in user.
        This is called from inside the SmartLoginActivity to save the
        current logged in user to the shared preferences.
    */
    static boolean setUserSession(Context context, SmartUser smartUser){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        if(smartUser != null) {
            try {
                preferences = context.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);
                editor = preferences.edit();

                if (smartUser instanceof SmartFacebookUser) {
                    editor.putString(Constants.USER_TYPE, Constants.FACEBOOKFLAG);
                } else if (smartUser instanceof SmartGoogleUser) {
                    editor.putString(Constants.USER_TYPE, Constants.GOOGLEFLAG);
                } else {
                    editor.putString(Constants.USER_TYPE, Constants.CUSTOMUSERFLAG);
                }

                Gson gson = new Gson();
                String sessionUser = gson.toJson(smartUser);
                Log.d("GSON", sessionUser);
                editor.putString(Constants.USER_SESSION, sessionUser);
                editor.apply();
                return true;
            } catch (Exception e) {
                Log.e("Session Error", e.getMessage());
                return false;
            }
        } else {
            Log.e("Session Error", "User is null");
            return false;
        }
    }
}
