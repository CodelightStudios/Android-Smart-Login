package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;

/**
 * Copyright (c) 2017 Codelight Studios
 * Created by kalyandechiraju on 22/04/17.
 */

public abstract class SmartLogin {

    public abstract void login(SmartLoginConfig config);

    public abstract void signup(SmartLoginConfig config);

    public abstract boolean logout(Context context);

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data, SmartLoginConfig config);

}
