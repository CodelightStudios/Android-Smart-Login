package studios.codelight.smartloginlibrary.users;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kalyan on 9/23/2015.
 */
public class SmartUser implements Parcelable{

    private String  userId;
    private String username;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String birthday;
    private int gender;
    private String profileLink;

    public SmartUser() {
    }

    protected SmartUser(Parcel in) {
        userId = in.readString();
        username = in.readString();
        password = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        email = in.readString();
        birthday = in.readString();
        gender = in.readInt();
        profileLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeInt(gender);
        dest.writeString(profileLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmartUser> CREATOR = new Creator<SmartUser>() {
        @Override
        public SmartUser createFromParcel(Parcel in) {
            return new SmartUser(in);
        }

        @Override
        public SmartUser[] newArray(int size) {
            return new SmartUser[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }
}
