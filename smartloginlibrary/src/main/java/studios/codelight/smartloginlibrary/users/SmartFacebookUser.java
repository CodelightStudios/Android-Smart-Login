package studios.codelight.smartloginlibrary.users;

import android.net.Uri;

/**
 * Created by Kalyan on 9/23/2015.
 */
public class SmartFacebookUser extends SmartUser {
    private String profileName;
    private String middleName;
    private Uri profileLink;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Uri getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(Uri profileLink) {
        this.profileLink = profileLink;
    }
}
