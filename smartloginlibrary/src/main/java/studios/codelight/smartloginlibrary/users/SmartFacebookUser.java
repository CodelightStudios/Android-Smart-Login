package studios.codelight.smartloginlibrary.users;

import com.facebook.AccessToken;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/23/2015.
 */
public class SmartFacebookUser extends SmartUser {
    private String profileName;
    private AccessToken accessToken;

    public SmartFacebookUser() {
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
