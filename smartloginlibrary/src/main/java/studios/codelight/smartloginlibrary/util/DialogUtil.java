package studios.codelight.smartloginlibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kalyan on 9/27/2015.
 */
public class DialogUtil {
    public static Dialog getErrorDialog(int errorCode, Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(errorCode);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }
}
