package studios.codelight.smartlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import studios.codelight.smartloginlibrary.SmartCustomLoginListener;
import studios.codelight.smartloginlibrary.SmartLoginBuilder;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.manager.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;


public class MainActivity extends AppCompatActivity {
    //SmartFacebookResult smartFacebookResult;
    TextView loginResult;
    CheckBox customLogin, facebookLogin, googleLogin, appLogoCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginResult = (TextView) findViewById(R.id.login_result);
        customLogin = (CheckBox) findViewById(R.id.customCheckbox);
        facebookLogin = (CheckBox) findViewById(R.id.facebookCheckbox);
        googleLogin = (CheckBox) findViewById(R.id.googleCheckbox);
        appLogoCheckBox = (CheckBox) findViewById(R.id.appLogoCheckbox);

        //get the current user details
        SmartUser currentUser = UserSessionManager.getCurrentUser(this);
        String display = "no user";
        if(currentUser != null) {
            if (currentUser instanceof SmartFacebookUser) {
                SmartFacebookUser facebookUser = (SmartFacebookUser) currentUser;
                display = facebookUser.getProfileName() + " (FacebookUser)is logged in";
            } else if (currentUser instanceof SmartGoogleUser) {
                display = ((SmartGoogleUser) currentUser).getDisplayName() + " (GoogleUser) is logged in";
            } else {
                display = currentUser.getUsername() + " (Custom User) is logged in";
            }
        }
        loginResult.setText(display);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartLoginBuilder loginBuilder = new SmartLoginBuilder();

                //Set facebook permissions
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add("public_profile");
                permissions.add("email");
                permissions.add("user_birthday");
                permissions.add("user_friends");


                Intent intent = loginBuilder.with(getApplicationContext())
                        .setAppLogo(getlogo())
                        .isFacebookLoginEnabled(facebookLogin.isChecked())
                        .withFacebookAppId(getString(R.string.facebook_app_id)).withFacebookPermissions(permissions)
                        .isGoogleLoginEnabled(googleLogin.isChecked())
                        .isCustomLoginEnabled(customLogin.isChecked()).setSmartCustomLoginHelper(new SmartCustomLoginListener() {
                            @Override
                            public boolean customSignin(SmartUser user) {
                                //This "user" will have only username and password set.
                                Toast.makeText(MainActivity.this, user.getUsername() + " " + user.getPassword(), Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            @Override
                            public boolean customSignup(SmartUser newUser) {
                                //Implement your our custom sign up logic and return true if success
                                return true;
                            }


                        })
                        .build();

                startActivityForResult(intent, SmartLoginConfig.FACEBOOK_LOGIN_REQUEST);
                //startActivity(intent);
            }
        });
    }

    private int getlogo() {
        if(appLogoCheckBox.isChecked()){
            return R.mipmap.ic_launcher;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fail = "Login Failed";
        if(resultCode == SmartLoginConfig.FACEBOOK_LOGIN_REQUEST){
            SmartFacebookUser user;
            try {
                user = data.getParcelableExtra(SmartLoginConfig.USER);
                String userDetails = user.getProfileName() + " " + user.getEmail() + " " + user.getBirthday();
                loginResult.setText(userDetails);
            }catch (Exception e){
                loginResult.setText(fail);
            }
        }
        else if(resultCode == SmartLoginConfig.GOOGLE_LOGIN_REQUEST){
            SmartGoogleUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getEmail() + " " + user.getBirthday() + " " + user.getAboutMe();
            loginResult.setText(userDetails);
        }
        else if(resultCode == SmartLoginConfig.CUSTOM_LOGIN_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }
        else if(resultCode == SmartLoginConfig.CUSTOM_SIGNUP_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }
        else if(resultCode == RESULT_CANCELED){
            loginResult.setText(fail);
        }

    }
}
