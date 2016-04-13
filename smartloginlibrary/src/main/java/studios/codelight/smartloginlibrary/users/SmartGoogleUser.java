package studios.codelight.smartloginlibrary.users;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/25/2015.
 */
public class SmartGoogleUser extends SmartUser implements Parcelable{

    private String displayName;
    private Uri photoUrl;
    //private String idToken;
    //private String serverAuthCode;

    public SmartGoogleUser() {
    }


    protected SmartGoogleUser(Parcel in) {
        super(in);
        displayName = in.readString();
        photoUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(displayName);
        dest.writeParcelable(photoUrl, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmartGoogleUser> CREATOR = new Creator<SmartGoogleUser>() {
        @Override
        public SmartGoogleUser createFromParcel(Parcel in) {
            return new SmartGoogleUser(in);
        }

        @Override
        public SmartGoogleUser[] newArray(int size) {
            return new SmartGoogleUser[size];
        }
    };

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }
}
