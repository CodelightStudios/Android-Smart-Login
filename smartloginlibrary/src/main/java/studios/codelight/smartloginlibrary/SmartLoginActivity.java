package studios.codelight.smartloginlibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.Arrays;

import studios.codelight.smartloginlibrary.users.SmartFacebookUser;

public class SmartLoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    CallbackManager callbackManager;
    LoginResult mFacebookLoginResult;
    SmartLoginConfig config;


    //Google Sign in related
    private GoogleApiClient mGoogleApiClient;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


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


        Bundle bundle = getIntent().getExtras();
        config = SmartLoginConfig.unpack(bundle);

        //set the listeners for the buttons
        findViewById(R.id.login_fb_button).setOnClickListener(this);
        findViewById(R.id.login_google_button).setOnClickListener(this);
        findViewById(R.id.custom_signin_button).setOnClickListener(this);
        findViewById(R.id.custom_signup_button).setOnClickListener(this);
    }

    //Required for Facebook login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("GOOGLE LOGIN", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == SmartLoginConfig.GOOGLE_LOGIN_REQUEST) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();

            //setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, data);
            //finish();
        }
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
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d("GOOGLE LOGIN", "onConnected:" + bundle);
        mShouldResolve = false;

        //Get Google profile info
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhoto = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();


            //SmartGoogleUser smartGoogleUser = new SmartGoogleUser();
            Intent data = new Intent();
            data.putExtra("currentUser", personName);
            setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, data);
            finish();
        }



        // Show the signed-in UI
        //showSignedInUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        final String TAG = "GOOGLE LOGIN";
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, SmartLoginConfig.GOOGLE_LOGIN_REQUEST);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
//                Intent intent = new Intent();
//                intent.putExtra("smartUser", "failed");
//                setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
//                finish();
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.login_fb_button){
            //do facebook login
            doFacebookLogin();
        } else if(id == R.id.login_google_button){
            //do google login
            //Toast.makeText(SmartLoginActivity.this, "Google login", Toast.LENGTH_SHORT).show();
            doGoogleLogin();
        } else if(id == R.id.custom_signin_button){
            //custom signin implementation
            doCustomSignin();
        } else if(id == R.id.custom_signup_button){
            //custom signup implementation
            doCustomSignup();
        }
    }

    private void doGoogleLogin() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    private void doCustomSignup() {
        final ProgressDialog progress = ProgressDialog.show(this, "", "Logging in...", true);
        if(SmartLoginBuilder.mSmartCustomLoginHelper != null) {
            if(SmartLoginBuilder.mSmartCustomLoginHelper.customSignup()){
                progress.dismiss();
                setResult(SmartLoginConfig.CUSTOM_SIGNUP_REQUEST);
            }else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    private void doCustomSignin() {
        if(SmartLoginBuilder.mSmartCustomLoginHelper != null) {
            final ProgressDialog progress = ProgressDialog.show(this, "", "Logging in...", true);
            if(SmartLoginBuilder.mSmartCustomLoginHelper.customSignin()) {
                progress.dismiss();
                setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST);
            }else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    private void doFacebookLogin() {
        if(config.isFacebookEnabled()) {
            Toast.makeText(SmartLoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
            final ProgressDialog progress = ProgressDialog.show(this, "", "Logging in...", true);
            LoginManager.getInstance().logInWithReadPermissions(SmartLoginActivity.this, Arrays.asList("public_profile", "user_friends"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    mFacebookLoginResult = loginResult;
                    progress.dismiss();

                    //Populate the SmartFacebookUser from Facebook's Profile object
                    Profile profile = Profile.getCurrentProfile();
                    SmartFacebookUser currentUser = new SmartFacebookUser();
                    currentUser.setUserId(profile.getId());
                    currentUser.setFirstName(profile.getFirstName());
                    currentUser.setLastName(profile.getLastName());
                    currentUser.setProfileName(profile.getName());
                    currentUser.setMiddleName(profile.getMiddleName());
                    currentUser.setProfileLink(profile.getLinkUri());

                    Intent intent = new Intent();
                    intent.putExtra("smartUser", currentUser);
                    setResult(SmartLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
                    finish();
                }

                @Override
                public void onCancel() {
                    progress.dismiss();
                    finish();
                    Log.d("Facebook Login", "User cancelled the login process");
                }

                @Override
                public void onError(FacebookException e) {
                    progress.dismiss();
                    finish();
                    Toast.makeText(SmartLoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
