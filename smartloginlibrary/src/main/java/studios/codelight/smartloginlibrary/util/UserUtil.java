package studios.codelight.smartloginlibrary.util;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 10/3/2015.
 */
public class UserUtil {

    public SmartGoogleUser populateGoogleUser(GoogleSignInAccount account){
        //Create a new google user
        SmartGoogleUser googleUser = new SmartGoogleUser();
        //populate the user
        googleUser.setDisplayName(account.getDisplayName());
        //googleUser.setIdToken(account.getIdToken());
        googleUser.setPhotoUrl(account.getPhotoUrl());
        googleUser.setEmail(account.getEmail());
        //googleUser.setServerAuthCode(account.getServerAuthCode());

        //return the populated google user
        return googleUser;
    }

    public SmartFacebookUser populateFacebookUser(JSONObject object){
        SmartFacebookUser facebookUser = new SmartFacebookUser();
        facebookUser.setGender(-1);
        try {
            if (object.has(SmartLoginConfig.FacebookFields.EMAIL))
                facebookUser.setEmail(object.getString(SmartLoginConfig.FacebookFields.EMAIL));
            if (object.has(SmartLoginConfig.FacebookFields.BIRTHDAY))
                facebookUser.setBirthday(object.getString(SmartLoginConfig.FacebookFields.BIRTHDAY));
            if (object.has(SmartLoginConfig.FacebookFields.GENDER)) {
                try {
                    SmartLoginConfig.Gender gender = SmartLoginConfig.Gender.valueOf(object.getString(SmartLoginConfig.FacebookFields.GENDER));
                    switch (gender) {
                        case male:
                            facebookUser.setGender(0);
                            break;
                        case female:
                            facebookUser.setGender(1);
                            break;
                    }
                } catch (Exception e) {
                    //if gender is not in the enum it is set to unspecified value (-1)
                    facebookUser.setGender(-1);
                    Log.e(getClass().getSimpleName(), e.getMessage());
                }
            }
            if (object.has(SmartLoginConfig.FacebookFields.LINK))
                facebookUser.setProfileLink(object.getString(SmartLoginConfig.FacebookFields.LINK));
            if (object.has(SmartLoginConfig.FacebookFields.ID))
                facebookUser.setUserId(object.getString(SmartLoginConfig.FacebookFields.ID));
            if (object.has(SmartLoginConfig.FacebookFields.NAME))
                facebookUser.setProfileName(object.getString(SmartLoginConfig.FacebookFields.NAME));
            if (object.has(SmartLoginConfig.FacebookFields.FIRST_NAME))
                facebookUser.setFirstName(object.getString(SmartLoginConfig.FacebookFields.FIRST_NAME));
            if (object.has(SmartLoginConfig.FacebookFields.MIDDLE_NAME))
                facebookUser.setMiddleName(object.getString(SmartLoginConfig.FacebookFields.MIDDLE_NAME));
            if (object.has(SmartLoginConfig.FacebookFields.LAST_NAME))
                facebookUser.setLastName(object.getString(SmartLoginConfig.FacebookFields.LAST_NAME));
        } catch (JSONException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
            facebookUser = null;
        }
        return facebookUser;
    }

    public SmartUser populateCustomUserWithUserName(String username, String email, String password){
        SmartUser user = new SmartUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(-1);
        return user;
    }

    public SmartUser populateCustomUserWithEmail(String username, String email, String password){
        SmartUser user = new SmartUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(-1);
        return user;
    }

}
