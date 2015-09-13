package studios.codelight.smartloginlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class SmartLoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginResult mLoginResult;
    SmartLoginConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_smart_login);

        callbackManager = CallbackManager.Factory.create();
        Bundle bundle = getIntent().getBundleExtra(getString(R.string.config_data));
        config = SmartLoginConfig.unpack(bundle);

        Button customSigninButton = (Button) findViewById(R.id.custom_signin_button);
        Button facebookLoginButton = (Button) findViewById(R.id.login_fb_button);
        Button twitterLoginButton = (Button) findViewById(R.id.login_twitter_button);
        Button customSignupButton = (Button) findViewById(R.id.custom_signup_button);

        if(config.isFacebookEnabled()) {
            doFacebookLogin(facebookLoginButton);
        }

        if(config.isTwitterEnabled()){
            doTwitterLogin(twitterLoginButton);
        }
        if(config.getLoginHelper() != null){
            doCustomSignin(customSigninButton);
        }

        if(config.getLoginHelper() != null){
            doCustomSignup(customSignupButton);
        }

    }

    private void doCustomSignup(Button customSignupButton) {
        customSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                config.getLoginHelper().customSignup();
            }
        });
    }

    private void doCustomSignin(Button customSigninButton) {
        customSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                config.getLoginHelper().customSignin();
            }
        });
    }

    private void doTwitterLogin(Button twitterLoginButton) {
        //Implement Twitter login
        twitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SmartLoginActivity.this, "Twitter login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doFacebookLogin(Button facebookLoginButton) {

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SmartLoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logInWithReadPermissions(SmartLoginActivity.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginResult = loginResult;
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    //Required for Facebook login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart_login, menu);
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

}
