package studios.codelight.smartloginlibrary.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kalyan on 9/27/2015.
 */
public class DialogUtil {
    public static Dialog getErrorDialog(int errorCode, Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorCode);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }
}
