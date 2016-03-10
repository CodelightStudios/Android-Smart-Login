package studios.codelight.smartloginlibrary.util;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Created by Kalyan on 10/3/2015.
 */
public class UserUtil {

    public SmartGoogleUser populateGoogleUser(Person person, GoogleApiClient googleApiClient){
        //Create a new google user
        SmartGoogleUser googleUser = new SmartGoogleUser();
        googleUser.setGender(-1);
        //populate the user
        if(person.hasName()) {
            Person.Name name = person.getName();
            if (name.hasGivenName())
                googleUser.setFirstName(name.getGivenName());
            if (name.hasFamilyName())
                googleUser.setLastName(name.getFamilyName());
            if (name.hasFormatted())
                googleUser.setFullName(name.getFormatted());
            if (name.hasMiddleName())
                googleUser.setMiddleName(name.getMiddleName());
        }
        if(person.hasId())
            googleUser.setUserId(person.getId());
        if(person.hasNickname())
            googleUser.setNickname(person.getNickname());
        if(person.hasDisplayName())
            googleUser.setDisplayName(person.getDisplayName());
        if(person.hasBirthday())
            googleUser.setBirthday(person.getBirthday());
        if(person.hasAboutMe())
            googleUser.setAboutMe(person.getAboutMe());
        if(person.hasLanguage())
            googleUser.setLanguage(person.getLanguage());
        if(person.hasGender())
            googleUser.setGender(person.getGender());
        if(person.hasBraggingRights())
            googleUser.setBraggingRights(person.getBraggingRights());
        String email = Plus.AccountApi.getAccountName(googleApiClient);
        if(email != null){
            googleUser.setEmail(email);
        }
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
