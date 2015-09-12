package studios.codelight.smartloginlibrary;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;

import java.util.Set;

/**
 * Created by Kalyan on 9/12/2015.
 */
public class SmartFacebookResult extends LoginResult {
    public SmartFacebookResult(AccessToken accessToken, Set<String> recentlyGrantedPermissions, Set<String> recentlyDeniedPermissions) {
        super(accessToken, recentlyGrantedPermissions, recentlyDeniedPermissions);
    }
}
