package studios.codelight.smartloginlibrary.users;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kalyan on 9/25/2015.
 */
public class SmartGoogleUser extends SmartUser implements Parcelable{

    private String displayName;
    private String aboutMe;
    private String nickname;
    private String braggingRights;
    private String fullName;
    private String language;

    public SmartGoogleUser() {
    }

    protected SmartGoogleUser(Parcel in) {
        super(in);
        displayName = in.readString();
        aboutMe = in.readString();
        nickname = in.readString();
        braggingRights = in.readString();
        fullName = in.readString();
        language = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(displayName);
        dest.writeString(aboutMe);
        dest.writeString(nickname);
        dest.writeString(braggingRights);
        dest.writeString(fullName);
        dest.writeString(language);
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

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBraggingRights() {
        return braggingRights;
    }

    public void setBraggingRights(String braggingRights) {
        this.braggingRights = braggingRights;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
