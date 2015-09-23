package studios.codelight.smartloginlibrary.users;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kalyan on 9/23/2015.
 */
public class SmartUser implements Parcelable{
    private String  userId;
    private String firstName;
    private String lastName;

    public SmartUser() {
    }

    protected SmartUser(Parcel in) {
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(lastName);
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
}
