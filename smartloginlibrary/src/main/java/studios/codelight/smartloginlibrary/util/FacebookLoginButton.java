package studios.codelight.smartloginlibrary.util;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatButton;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 10/09/16.
 */
public class FacebookLoginButton extends AppCompatButton implements Parcelable{
    private Context context;
    public FacebookLoginButton(Context context) {
        super(context);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FacebookLoginButton> CREATOR = new Creator<FacebookLoginButton>() {
        @Override
        public FacebookLoginButton createFromParcel(Parcel in) {
            return new FacebookLoginButton(null);
        }

        @Override
        public FacebookLoginButton[] newArray(int size) {
            return new FacebookLoginButton[size];
        }
    };
}
