package studios.codelight.smartloginlibrary.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.AccessToken;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/23/2015.
 */
public class SmartFacebookUser extends SmartUser implements Parcelable {
    private String profileName;
    private AccessToken accessToken;

    public SmartFacebookUser() {
    }

    protected SmartFacebookUser(Parcel in) {
        super(in);
        profileName = in.readString();
        accessToken = in.readParcelable(AccessToken.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(profileName);
        dest.writeParcelable(accessToken, flags);
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

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
