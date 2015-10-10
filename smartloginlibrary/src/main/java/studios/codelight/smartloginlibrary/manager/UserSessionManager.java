package studios.codelight.smartloginlibrary.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import studios.codelight.smartloginlibrary.R;
import studios.codelight.smartloginlibrary.SmartLoginActivity;
import studios.codelight.smartloginlibrary.SmartLoginBuilder;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.DialogUtil;

/**
 * Created by Kalyan on 9/29/2015.
 */
public class UserSessionManager {

    static final String USER_SESSION = "user_session_key";
    static final String USER_PREFS = "codelight_studios_user_prefs";
    static final String DEFAULT_SESSION_VALUE = "No logged in user";

    public static SmartUser getCurrentUser(Context context){
        SmartUser smartUser = null;
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String sessionUser = preferences.getString(USER_SESSION, DEFAULT_SESSION_VALUE);
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
            editor.putString(USER_SESSION, sessionUser);
            editor.apply();
            return true;
        } catch (Exception e){
            Log.e("Session Error", e.getMessage());
            return false;
        }
    }

    public static boolean logout(Activity context, SmartUser user){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        try {
            preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            editor = preferences.edit();

            try {
                String user_type = preferences.getString(SmartLoginConfig.USER_TYPE, SmartLoginConfig.CUSTOMUSERFLAG);
                switch (user_type) {
                    case SmartLoginConfig.FACEBOOKFLAG:
                        LoginManager.getInstance().logOut();
                        break;
                    case SmartLoginConfig.GOOGLEFLAG:
                        GoogleApiClient mGoogleApiClient = SmartLoginActivity.getGoogleApiClient();
                        if(mGoogleApiClient != null) {
                            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                            mGoogleApiClient.disconnect();
                        }
                        break;
                    case SmartLoginConfig.CUSTOMUSERFLAG:
                        if(!SmartLoginBuilder.smartCustomLoginListener.customUserSignout(user)){
                            throw new Exception("User not logged out");
                        }
                        break;
                    default:
                        break;
                }

                editor.remove(SmartLoginConfig.USER_TYPE);
                editor.remove(USER_SESSION);
                editor.apply();
                return true;
            } catch (Exception e){
                Log.e("User Logout Error", e.getMessage());
                DialogUtil.getErrorDialog(R.string.network_error, context);
                return false;
            }

        } catch (Exception e){
            Log.e("User Logout Error", e.getMessage());
            return false;
        }
    }
}
