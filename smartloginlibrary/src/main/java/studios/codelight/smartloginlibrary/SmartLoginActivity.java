package studios.codelight.smartloginlibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONObject;

import java.util.ArrayList;

import studios.codelight.smartloginlibrary.manager.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.DialogUtil;
import studios.codelight.smartloginlibrary.util.UserUtil;

public class SmartLoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    CallbackManager callbackManager;
    SmartLoginConfig config;
    EditText usernameEditText, passwordEditText, usernameSignup, emailSignup, passwordSignup, repeatPasswordSignup;
    ProgressDialog progress;
    //LinearLayout signUpPanel;
    ViewGroup mContainer;
    LinearLayout signinContainer, signupContainer;
    ImageView appLogo;


    //Google Sign in related
    private GoogleApiClient mGoogleApiClient;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the config object from the intent and unpack it
        Bundle bundle = getIntent().getExtras();
        config = SmartLoginConfig.unpack(bundle);

        FacebookSdk.setApplicationId(config.getFacebookAppId());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_smart_login);

        mContainer = (ViewGroup) findViewById(R.id.main_container);
        signinContainer = (LinearLayout) findViewById(R.id.signin_container);
        signupContainer = (LinearLayout) findViewById(R.id.signup_container);

        //Attach the views in the respective containers
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //include views based on user settings
        if(config.isCustomLoginEnabled()){
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_custom_login, mContainer, false));
            if(config.isFacebookEnabled() || config.isGoogleEnabled()) {
                signinContainer.addView(layoutInflater.inflate(R.layout.fragment_divider, mContainer, false));
            }
            signupContainer.addView(layoutInflater.inflate(R.layout.fragment_signup, mContainer, false));

            //listeners
            findViewById(R.id.custom_signin_button).setOnClickListener(this);
            findViewById(R.id.custom_signup_button).setOnClickListener(this);
            findViewById(R.id.user_signup_button).setOnClickListener(this);

            //Hide necessary views
            signupContainer.setVisibility(View.GONE);
        }

        if(config.isFacebookEnabled()){
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_facebook_login, mContainer, false));
            Button facebookButton = (Button) findViewById(R.id.login_fb_button);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_vector, 0, 0, 0);
            } else {
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white_36dp, 0, 0, 0);
            }
            facebookButton.setOnClickListener(this);
        }

        if(config.isGoogleEnabled()){
            /*if(!config.isCustomLoginEnabled() && config.isFacebookEnabled()){
                signinContainer.addView(layoutInflater.inflate(R.layout.fragment_divider, mContainer, false));
            }*/
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_google_login, mContainer, false));
            Button googlePlusButton = (Button) findViewById(R.id.login_google_button);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                googlePlusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_plus_vector, 0, 0, 0);
            } else {
                googlePlusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_plus_white_36dp, 0, 0, 0);
            }
            googlePlusButton.setOnClickListener(this);
        }

        //bind the views
        appLogo = (ImageView) findViewById(R.id.applogo_imageView);
        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameSignup = (EditText) findViewById(R.id.userNameSignUp);
        passwordSignup = (EditText) findViewById(R.id.passwordSignUp);
        repeatPasswordSignup = (EditText) findViewById(R.id.repeatPasswordSignUp);
        emailSignup = (EditText) findViewById(R.id.emailSignUp);

        //Set app logo
        if(config.getAppLogo() != 0) {
            appLogo.setImageResource(config.getAppLogo());
        } else {
            appLogo.setVisibility(View.GONE);
        }

        //Facebook login callback
        callbackManager = CallbackManager.Factory.create();

        //Google signin requirements
        //Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.EMAIL))
                .build();


        //signUpPanel = (LinearLayout) findViewById(R.id.signup_panel);
    }

    //Required for Facebook and google login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //for facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //For google login
        if (requestCode == SmartLoginConfig.GOOGLE_LOGIN_REQUEST) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
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

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.disconnect();
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
        progress = ProgressDialog.show(this, "", "Logging in...", true);

        //Get Google profile info
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            //Get the Person object of the current logged in user.
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            //From that obtained Person object populate the SmartGoogleUser object
            UserUtil util = new UserUtil();
            SmartGoogleUser googleUser = util.populateGoogleUser(currentPerson, mGoogleApiClient);
            progress.dismiss();
            finishLogin(googleUser);
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
        progress.dismiss();

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
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
                /*
                Intent intent = new Intent();
                intent.putExtra("smartUser", "failed");
                setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
                finish();
                */
            }
        }
        /*
        else {
             Show the signed-out UI
            showSignedOutUI();
        }
        */
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.login_fb_button){
            //do facebook login
            doFacebookLogin();
        } else if(id == R.id.login_google_button){
            //do google login
            doGoogleLogin();
        } else if(id == R.id.custom_signin_button){
            //custom signin implementation
            doCustomSignin();
        } else if(id == R.id.custom_signup_button){
            //custom signup implementation
            //AnimUtil.slideToTop(signinContainer);
            signinContainer.setVisibility(View.GONE);
            signupContainer.setVisibility(View.VISIBLE);
            findViewById(R.id.userNameSignUp).requestFocus();
        } else if(id == R.id.user_signup_button){
            doCustomSignup();
        }
    }

    private void doGoogleLogin() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    private void doCustomSignup() {
        String username = usernameSignup.getText().toString();
        String password = passwordSignup.getText().toString();
        String repeatPassword = repeatPasswordSignup.getText().toString();
        String email = emailSignup.getText().toString();
        if(username.equals("")){
            DialogUtil.getErrorDialog(R.string.username_error, this).show();
        } else if(password.equals("")) {
            DialogUtil.getErrorDialog(R.string.password_error, this).show();
        } else if(email.equals("")){
            DialogUtil.getErrorDialog(R.string.no_email_error, this).show();
        } else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            DialogUtil.getErrorDialog(R.string.invalid_email_error, this).show();
        } else if(!password.equals(repeatPassword)){
            DialogUtil.getErrorDialog(R.string.password_mismatch, this).show();
        }
        else {
            if (SmartLoginBuilder.smartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.loading_holder), true);
                SmartUser newUser = new UserUtil().populateCustomUser(username, email, password);
                if (SmartLoginBuilder.smartCustomLoginListener.customSignup(newUser)) {
                    progress.dismiss();
                    setResult(SmartLoginConfig.CUSTOM_SIGNUP_REQUEST);
                    finishLogin(newUser);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }

    }

    private void doCustomSignin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(username.equals("")){
            DialogUtil.getErrorDialog(R.string.username_error, this).show();
        } else if(password.equals("")){
            DialogUtil.getErrorDialog(R.string.password_error, this).show();
        } else {
            if (SmartLoginBuilder.smartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
                SmartUser user = new UserUtil().populateCustomUser(username, null, password);
                if (SmartLoginBuilder.smartCustomLoginListener.customSignin(user)) {
                    progress.dismiss();
                    setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST);
                    finishLogin(user);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    private void doFacebookLogin() {
        if(config.isFacebookEnabled()) {
            Toast.makeText(SmartLoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
            final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
            ArrayList<String> permissions = config.getFacebookPermissions();
            if (permissions == null){
                permissions = SmartLoginConfig.getDefaultFacebookPermissions();
            }
            LoginManager.getInstance().logInWithReadPermissions(SmartLoginActivity.this, permissions);
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    progress.setMessage(getString(R.string.getting_data));
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            progress.dismiss();
                            UserUtil util = new UserUtil();
                            SmartFacebookUser facebookUser = util.populateFacebookUser(object);
                            if(facebookUser != null){
                                finishLogin(facebookUser);
                            } else {
                                finish();
                            }
                        }
                    });
                    request.executeAsync();
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

    private void finishLogin(SmartUser smartUser){
        UserSessionManager sessionManager = new UserSessionManager();
        if(sessionManager.setUserSession(this, smartUser)){
            Intent intent = new Intent();
            intent.putExtra(SmartLoginConfig.USER, smartUser);
            if(smartUser instanceof SmartFacebookUser) {
                setResult(SmartLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
            } else if(smartUser instanceof SmartGoogleUser) {
                setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
            } else {
                setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST, intent);
            }
            finish();
        } else {
            DialogUtil.getErrorDialog(R.string.network_error, this);
        }
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(signupContainer.getVisibility() == View.VISIBLE && config.isCustomLoginEnabled()){
            signupContainer.setVisibility(View.GONE);
            signinContainer.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }
}
