package studios.codelight.smartloginlibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
        GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 105;
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
    /* Is there a ConnectionResult resolution in progress? *//*
    private boolean mIsResolving = false;

    *//* Should we automatically resolve ConnectionResults when possible? *//*
    private boolean mShouldResolve = false;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the config object from the intent and unpack it
        Bundle bundle = getIntent().getExtras();
        config = SmartLoginConfig.unpack(bundle);

        //Set the facebook app id and initialize sdk
        FacebookSdk.setApplicationId(config.getFacebookAppId());
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Attaching the view
        setContentView(R.layout.activity_smart_login);

        //Set the title and back button on the Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Get the containers required to inject the views
        mContainer = (ViewGroup) findViewById(R.id.main_container);
        signinContainer = (LinearLayout) findViewById(R.id.signin_container);
        signupContainer = (LinearLayout) findViewById(R.id.signup_container);

        //Inject the views in the respective containers
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
            AppCompatButton facebookButton = (AppCompatButton) findViewById(R.id.login_fb_button);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_vector, 0, 0, 0);
            } else {
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white_36dp, 0, 0, 0);
            }
            facebookButton.setOnClickListener(this);
        }

        if(config.isGoogleEnabled()){
            //Decided to remove divider between two social buttons when there is no custom sign in option
            /*if(!config.isCustomLoginEnabled() && config.isFacebookEnabled()){
                signinContainer.addView(layoutInflater.inflate(R.layout.fragment_divider, mContainer, false));
            }*/
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_google_login, mContainer, false));
            AppCompatButton googlePlusButton = (AppCompatButton) findViewById(R.id.login_google_button);
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
    }

    //Required for Facebook and google login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //for facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            progress = ProgressDialog.show(this, "", getString(R.string.getting_data), true);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("GOOGLE SIGN IN", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                UserUtil util = new UserUtil();
                SmartGoogleUser googleUser = util.populateGoogleUser(acct);
                progress.dismiss();
                finishLogin(googleUser);
            } else {
                Log.d("GOOGLE SIGN IN", ""+requestCode);
                // Signed out, show unauthenticated UI.
                progress.dismiss();
                Toast.makeText(SmartLoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show();
                finishLogin(null);
            }
        }
        if(progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        final String TAG = "GOOGLE LOGIN";
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        progress.dismiss();
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        finish();
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
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* On~ConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progress.dismiss();
        //mGoogleApiClient.connect();
    }

    private void doCustomSignup() {
        String username = usernameSignup.getText().toString();
        String password = passwordSignup.getText().toString();
        String repeatPassword = repeatPasswordSignup.getText().toString();
        String email = emailSignup.getText().toString();
        if(username.equals("")){
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            usernameSignup.setError(getResources().getText(R.string.username_error));
            usernameSignup.requestFocus();
        } else if(password.equals("")) {
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            passwordSignup.setError(getResources().getText(R.string.password_error));
            passwordSignup.requestFocus();
        } else if(email.equals("")){
            //DialogUtil.getErrorDialog(R.string.no_email_error, this).show();
            emailSignup.setError(getResources().getText(R.string.no_email_error));
            emailSignup.requestFocus();
        } else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //DialogUtil.getErrorDialog(R.string.invalid_email_error, this).show();
            emailSignup.setError(getResources().getText(R.string.invalid_email_error));
            emailSignup.requestFocus();
        } else if(!password.equals(repeatPassword)){
            //DialogUtil.getErrorDialog(R.string.password_mismatch, this).show();
            repeatPasswordSignup.setError(getResources().getText(R.string.password_mismatch));
            repeatPasswordSignup.requestFocus();
        }
        else {
            if (SmartLoginBuilder.smartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.loading_holder), true);
                SmartUser newUser = new UserUtil().populateCustomUserWithUserName(username, email, password);
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
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            if(config.getLoginType() == SmartLoginConfig.LoginType.withUsername) {
                usernameEditText.setError(getResources().getText(R.string.username_error));
            } else {
                usernameEditText.setError(getResources().getText(R.string.email_error));
            }
            usernameEditText.requestFocus();
        } else if(password.equals("")){
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            passwordEditText.setError(getResources().getText(R.string.password_error));
            passwordEditText.requestFocus();
        } else {
            if (SmartLoginBuilder.smartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
                SmartUser user;
                if(config.getLoginType() == SmartLoginConfig.LoginType.withUsername) {
                    user = new UserUtil().populateCustomUserWithUserName(username, null, password);
                } else {
                    user = new UserUtil().populateCustomUserWithEmail(null, username, password);
                }
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
            if (permissions == null) {
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
                            if (facebookUser != null) {
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
        if(smartUser != null) {
            UserSessionManager sessionManager = new UserSessionManager();
            if (sessionManager.setUserSession(this, smartUser)) {
                Intent intent = new Intent();
                intent.putExtra(SmartLoginConfig.USER, smartUser);
                if (smartUser instanceof SmartFacebookUser) {
                    setResult(SmartLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
                } else if (smartUser instanceof SmartGoogleUser) {
                    setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
                } else {
                    setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST, intent);
                }
                finish();
            } else {
                DialogUtil.getErrorDialog(R.string.network_error, this);
                finish();
            }
        } else {
            DialogUtil.getErrorDialog(R.string.login_failed, this);
            finish();
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

    /*public static GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }*/
}
