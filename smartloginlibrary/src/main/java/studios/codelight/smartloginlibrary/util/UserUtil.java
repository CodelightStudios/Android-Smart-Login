package studios.codelight.smartloginlibrary.util;

import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 10/3/2015.
 */
public class UserUtil {

    public static SmartGoogleUser populateGoogleUser(GoogleSignInAccount account){
        //Create a new google user
        SmartGoogleUser googleUser = new SmartGoogleUser();
        //populate the user
        googleUser.setDisplayName(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            googleUser.setPhotoUrl(account.getPhotoUrl().toString());
        }
        googleUser.setEmail(account.getEmail());
        googleUser.setIdToken(account.getIdToken());
        googleUser.setUserId(account.getId());
        if (account.getAccount() != null) {
            googleUser.setUsername(account.getAccount().name);
        }

        //return the populated google user
        return googleUser;
    }

    public static SmartFacebookUser populateFacebookUser(JSONObject object, AccessToken accessToken){
        SmartFacebookUser facebookUser = new SmartFacebookUser();
        facebookUser.setGender(-1);
        facebookUser.setAccessToken(accessToken);
        try {
            if (object.has(Constants.FacebookFields.EMAIL))
                facebookUser.setEmail(object.getString(Constants.FacebookFields.EMAIL));
            if (object.has(Constants.FacebookFields.BIRTHDAY))
                facebookUser.setBirthday(object.getString(Constants.FacebookFields.BIRTHDAY));
            if (object.has(Constants.FacebookFields.GENDER)) {
                try {
                    Constants.Gender gender = Constants.Gender.valueOf(object.getString(Constants.FacebookFields.GENDER));
                    switch (gender) {
                        case male:
                            facebookUser.setGender(0);
                            break;
                        case female:
                            facebookUser.setGender(1);
                            break;
                    }
                } catch (Exception e) {
                    //if gender is not in the enum it is already set to unspecified value (-1)
                    Log.e("UserUtil", e.getMessage());
                }
            }
            if (object.has(Constants.FacebookFields.LINK))
                facebookUser.setProfileLink(object.getString(Constants.FacebookFields.LINK));
            if (object.has(Constants.FacebookFields.ID))
                facebookUser.setUserId(object.getString(Constants.FacebookFields.ID));
            if (object.has(Constants.FacebookFields.NAME))
                facebookUser.setProfileName(object.getString(Constants.FacebookFields.NAME));
            if (object.has(Constants.FacebookFields.FIRST_NAME))
                facebookUser.setFirstName(object.getString(Constants.FacebookFields.FIRST_NAME));
            if (object.has(Constants.FacebookFields.MIDDLE_NAME))
                facebookUser.setMiddleName(object.getString(Constants.FacebookFields.MIDDLE_NAME));
            if (object.has(Constants.FacebookFields.LAST_NAME))
                facebookUser.setLastName(object.getString(Constants.FacebookFields.LAST_NAME));
        } catch (JSONException e){
            Log.e("UserUtil", e.getMessage());
            facebookUser = null;
        }
        return facebookUser;
    }

    public static SmartUser populateCustomUser(String username, String email, String userId){
        SmartUser user = new SmartUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setUserId(userId);
        return user;
    }

}
