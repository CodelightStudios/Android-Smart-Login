package studios.codelight.smartloginlibrary;

import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyan on 10-03-2016.
 */
public interface SmartCustomLogoutListener {
    boolean customUserSignout(SmartUser smartUser);
}
