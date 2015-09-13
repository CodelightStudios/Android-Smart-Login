package studios.codelight.smartlogin;

import android.util.Log;

import studios.codelight.smartloginlibrary.SmartLoginHelper;

/**
 * Created by Kalyan on 9/11/2015.
 */
public class LoginHelper extends SmartLoginHelper {

//    public LoginHelper(Context context) {
//        super(context);
//    }

    @Override
    protected void customSignin() {
        //Toast.makeText(context, "Custom login", Toast.LENGTH_SHORT).show();
        Log.d("Custom", "Custom login");
    }

    @Override
    protected void customSignup() {
        //Toast.makeText(context, "Custom signup", Toast.LENGTH_SHORT).show();
        Log.d("Custom", "Custom signup");
    }
}
