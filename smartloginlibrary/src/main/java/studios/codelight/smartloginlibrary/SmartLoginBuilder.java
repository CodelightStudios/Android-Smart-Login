package studios.codelight.smartloginlibrary;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginBuilder {


    public Intent build(Context context){
        return new Intent(context, SmartLoginActivity.class);
    }

}
