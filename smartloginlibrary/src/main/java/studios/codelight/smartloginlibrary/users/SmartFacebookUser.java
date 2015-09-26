package studios.codelight.smartloginlibrary.users;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kalyan on 9/23/2015.
 */
public class SmartFacebookUser extends SmartUser implements Parcelable {
    private String profileName;
    private Uri profileLink;

    public SmartFacebookUser() {
    }

    protected SmartFacebookUser(Parcel in) {
        super(in);
        profileName = in.readString();
        profileLink = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(profileName);
        dest.writeParcelable(profileLink, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmartFacebookUser> CREATOR = new Creator<SmartFacebookUser>() {
        @Override
        public SmartFacebookUser createFromParcel(Parcel in) {
            return new SmartFacebookUser(in);
        }

        @Override
        public SmartFacebookUser[] newArray(int size) {
            return new SmartFacebookUser[size];
        }
    };

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Uri getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(Uri profileLink) {
        this.profileLink = profileLink;
    }
}
