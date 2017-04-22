package studios.codelight.smartloginlibrary.users;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/25/2015.
 */
public class SmartGoogleUser extends SmartUser {

    private String displayName;
    private String photoUrl;
    private String idToken;

    public SmartGoogleUser() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
