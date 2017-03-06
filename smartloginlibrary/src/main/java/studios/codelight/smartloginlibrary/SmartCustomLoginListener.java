package studios.codelight.smartloginlibrary;

import java.io.Serializable;

import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/11/2015.
 */
public interface SmartCustomLoginListener extends Serializable {
    boolean customSignin(SmartUser user);
    void customSignup();
}
