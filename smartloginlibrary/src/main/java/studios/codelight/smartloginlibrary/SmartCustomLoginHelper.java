package studios.codelight.smartloginlibrary;

import java.io.Serializable;

/**
 * Created by Kalyan on 9/11/2015.
 */
public abstract class SmartCustomLoginHelper implements Serializable {
//    protected Context context;
//
//    public Context getContext() {
//        return context;
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }

    //    public SmartCustomLoginHelper(Context context) {
//        this.context = context;
//    }

    protected abstract void customSignin();
    protected abstract void customSignup();
}
