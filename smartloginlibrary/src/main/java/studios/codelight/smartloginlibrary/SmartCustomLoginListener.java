package studios.codelight.smartloginlibrary;

import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Created by Kalyan on 9/11/2015.
 */
public interface SmartCustomLoginListener {
    boolean customSignin(SmartUser user);
    boolean customSignup(SmartUser newUser);
    boolean customUserSignout(SmartUser smartUser);
}
