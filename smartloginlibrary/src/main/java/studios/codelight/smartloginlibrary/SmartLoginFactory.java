package studios.codelight.smartloginlibrary;

/**
 * Copyright (c) 2017 Codelight Studios
 * Created by kalyandechiraju on 22/04/17.
 */

public class SmartLoginFactory {
    public static SmartLogin build(LoginType loginType) {
        switch (loginType) {
            case Facebook:
                return new FacebookLogin();
            case Google:
                return new GoogleLogin();
            case CustomLogin:
                return new CustomLogin();
            default:
                // To avoid null pointers
                return new CustomLogin();
        }
    }
}
