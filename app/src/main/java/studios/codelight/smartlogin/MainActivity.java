package studios.codelight.smartlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginResult = (TextView) findViewById(R.id.login_result);

        //get the current user details
        SmartUser currentUser = UserSessionManager.getCurrentUser(this);
        String display = "no user";
        if(currentUser != null) {
            if (currentUser instanceof SmartFacebookUser) {
                SmartFacebookUser facebookUser = (SmartFacebookUser) currentUser;
                display = facebookUser.getProfileName() + " (FacebookUser)is logged in";
            } else if (currentUser instanceof SmartGoogleUser) {
                display = ((SmartGoogleUser) currentUser).getDisplayName() + " (GoogleUser) is logged in";
            }
            //display = currentUser.getUserId() + " is logged in";
        }
        loginResult.setText(display);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartLoginBuilder loginBuilder = new SmartLoginBuilder();


                Intent intent = loginBuilder.with(getApplicationContext())
                        .isFacebookLoginEnabled(true).withFacebookAppId(getString(R.string.facebook_app_id))
                        .isGoogleLoginEnabled(false)
                        .setmSmartCustomLoginHelper(new SmartCustomLoginListener() {
                            @Override
                            public boolean customSignin(String username, String password) {
                                Toast.makeText(MainActivity.this, username + " " + password, Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        })
                        .build();

                startActivityForResult(intent, SmartLoginConfig.FACEBOOK_LOGIN_REQUEST);
                //startActivity(intent);
            }
        });
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
        if(resultCode == SmartLoginConfig.FACEBOOK_LOGIN_REQUEST){
            SmartFacebookUser user;
            try {
                user = data.getParcelableExtra(SmartLoginConfig.USER);
                String userDetails = user.getProfileName() + " " + user.getEmail() + " " + user.getBirthday();
                loginResult.setText(userDetails);
            }catch (Exception e){
                loginResult.setText("login failed");
            }
        }
        else if(resultCode == SmartLoginConfig.GOOGLE_LOGIN_REQUEST){
            SmartGoogleUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getEmail() + " " + user.getBirthday() + " " + user.getAboutMe();
            loginResult.setText(userDetails);
        }
        else if(resultCode == SmartLoginConfig.CUSTOM_LOGIN_REQUEST){
            loginResult.setText("Custom logged in");
        }
        else if(resultCode == SmartLoginConfig.CUSTOM_SIGNUP_REQUEST){
            loginResult.setText("Custom Signed up");
        }
        else if(resultCode == RESULT_CANCELED){
            loginResult.setText("login failed");
        }

    }
}
