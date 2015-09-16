package studios.codelight.smartloginlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.Arrays;

public class SmartLoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    CallbackManager callbackManager;
    LoginResult mLoginResult;
    SmartLoginConfig config;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_smart_login);

        //Facebook login callback
        callbackManager = CallbackManager.Factory.create();

        //Google signin requirements
        //Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        Bundle bundle = getIntent().getBundleExtra(getString(R.string.config_data));
        config = SmartLoginConfig.unpack(bundle);

        //set the listeners for the buttons
        findViewById(R.id.login_fb_button).setOnClickListener(this);
        findViewById(R.id.login_twitter_button).setOnClickListener(this);
        findViewById(R.id.login_google_button).setOnClickListener(this);
        findViewById(R.id.custom_signin_button).setOnClickListener(this);
        findViewById(R.id.custom_signup_button).setOnClickListener(this);
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

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.login_fb_button){
            //do facebook login
            doFacebookLogin();
        } else if(id == R.id.login_twitter_button){
            //do twitter login
            doTwitterLogin();
        } else if(id == R.id.login_google_button){
            //do google login
            Toast.makeText(SmartLoginActivity.this, "Google login", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.custom_signin_button){
            //custom signin implementation
            doCustomSignin();
        } else if(id == R.id.custom_signup_button){
            //custom signup implementation
            doCustomSignup();
        }
    }

    private void doCustomSignup() {
        if(config.getLoginHelper() != null) {
            Toast.makeText(SmartLoginActivity.this, "Custom signup", Toast.LENGTH_SHORT).show();
            config.getLoginHelper().customSignup();
        }
    }

    private void doCustomSignin() {
        if(config.getLoginHelper() != null) {
            Toast.makeText(SmartLoginActivity.this, "Custom login", Toast.LENGTH_SHORT).show();
            config.getLoginHelper().customSignin();
        }
    }

    private void doTwitterLogin() {
        //Implement Twitter login
        if(config.isTwitterEnabled()) {
            Toast.makeText(SmartLoginActivity.this, "Twitter login", Toast.LENGTH_SHORT).show();
        }
    }

    private void doFacebookLogin() {
        if(config.isFacebookEnabled()) {
            Toast.makeText(SmartLoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logInWithReadPermissions(SmartLoginActivity.this, Arrays.asList("public_profile", "user_friends"));
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
    }
}
