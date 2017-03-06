package studios.codelight.smartloginlibrary.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Patterns;
import android.widget.EditText;

import studios.codelight.smartloginlibrary.R;
import studios.codelight.smartloginlibrary.SmartLoginActivity;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.UserUtil;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by kalyandechiraju on 26/09/16.
 */

public class CustomLogin implements BaseLogin {
    private EditText emailEdittext, passwordEdittext;

    public CustomLogin with(EditText emailEdittext, EditText passwordEdittext) {
        this.emailEdittext = emailEdittext;
        this.passwordEdittext = passwordEdittext;
        return this;
    }

    @Override
    public void handleLoginRequest(Activity activity, SmartLoginConfig config) {
        String email = emailEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();
        if (email.equals("")) {
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            if (config.getLoginType() == SmartLoginConfig.LoginType.withUsername) {
                emailEdittext.setError(activity.getResources().getText(R.string.username_error));
            } else {
                emailEdittext.setError(activity.getResources().getText(R.string.email_error));
            }
            emailEdittext.requestFocus();
        } else if (config.getLoginType() == SmartLoginConfig.LoginType.withEmail && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdittext.setError(activity.getResources().getText(R.string.invalid_email_error));
            emailEdittext.requestFocus();
        } else if (password.equals("")) {
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            passwordEdittext.setError(activity.getResources().getText(R.string.password_error));
            passwordEdittext.requestFocus();
        } else {
            if (activity != null) {
                final ProgressDialog progress = ProgressDialog.show(activity, "", activity.getString(R.string.logging_holder), true);
                SmartUser user;
                if (config.getLoginType() == SmartLoginConfig.LoginType.withUsername) {
                    user = new UserUtil().populateCustomUser(email, null, password);
                } else {
                    user = new UserUtil().populateCustomUser(null, email, password);
                }
                if (config.getLoginListener() != null && config.getLoginListener().customSignin(user)) {
                    progress.dismiss();
                    activity.setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST);
                    ((SmartLoginActivity) activity).finishLogin(user);
                } else {
                    progress.dismiss();
                    activity.setResult(Activity.RESULT_CANCELED);
                    activity.finish();
                }
            }
        }
    }
}
