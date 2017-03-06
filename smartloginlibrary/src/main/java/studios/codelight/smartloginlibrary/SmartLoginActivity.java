package studios.codelight.smartloginlibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import studios.codelight.smartloginlibrary.login.CustomLogin;
import studios.codelight.smartloginlibrary.login.CustomSignUp;
import studios.codelight.smartloginlibrary.login.FacebookLogin;
import studios.codelight.smartloginlibrary.login.GoogleLogin;
import studios.codelight.smartloginlibrary.manager.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.DialogUtil;
import studios.codelight.smartloginlibrary.util.UserUtil;

public class SmartLoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int GOOGLE_SIGN_IN_REQUEST = 105;
    private CallbackManager callbackManager;
    private SmartLoginConfig config;
    private ProgressDialog progress;
    private Button signInButton, signUpButton, facebookButton, googleButton;
    EditText emailEdittext, passwordEdittext;


    //Google Sign in related
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int theme = getIntent().getIntExtra(SmartLoginConfig.LOGIN_THEME, 0);
        if (theme != 0) {
            setTheme(theme);
        }
        super.onCreate(savedInstanceState);

        //get the config object from the intent and unpack it
        Bundle bundle = getIntent().getExtras();
        config = SmartLoginConfig.unpack(bundle);

        //Set the facebook app id and initialize sdk
        FacebookSdk.setApplicationId(config.getFacebookAppId());

        int layoutId = getIntent().getIntExtra(SmartLoginConfig.LAYOUT_ID, 0);

        if (layoutId != 0) {
            //View view = LayoutInflater.from(this).inflate(layoutId, null);
            View rootView = LayoutInflater.from(this).inflate(layoutId, null);
            setContentView(rootView);
            // Bind the available buttons
            bindViews(rootView);
            // Set onClick listeners for the buttons
            setOnClickListeners();

        } else {
            setResult(SmartLoginConfig.FATAL);
            finish();
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso;
        if (config.getGoogleAppServerClientId() != null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .requestIdToken(config.getGoogleAppServerClientId())
                    .build();
        } else {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .build();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* On~ConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Set the title and back button on the Action bar
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.smart_login_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        //Get the containers required to inject the views
        /*mContainer = (ViewGroup) findViewById(R.id.main_container);
        signinContainer = (LinearLayout) findViewById(R.id.signin_container);
        signupContainer = (LinearLayout) findViewById(R.id.signup_container);*/

        //Inject the views in the respective containers
        //LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //include views based on user settings
        /*if(config.isCustomLoginEnabled()){
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
        }*/

        /*if(config.isFacebookEnabled()){
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_facebook_login, mContainer, false));
            AppCompatButton facebookButton = (AppCompatButton) findViewById(R.id.facebook_login_button);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_vector, 0, 0, 0);
            } else {
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white_36dp, 0, 0, 0);
            }
            facebookButton.setOnClickListener(this);
        }

        if(config.isGoogleEnabled()){
            //Decided to remove divider between two social buttons when there is no custom sign in option
            *//*if(!config.isCustomLoginEnabled() && config.isFacebookEnabled()){
                signinContainer.addView(layoutInflater.inflate(R.layout.fragment_divider, mContainer, false));
            }*//*
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_google_login, mContainer, false));
            AppCompatButton googlePlusButton = (AppCompatButton) findViewById(R.id.google_login_button);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                googlePlusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_plus_vector, 0, 0, 0);
            } else {
                googlePlusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_plus_white_36dp, 0, 0, 0);
            }
            googlePlusButton.setOnClickListener(this);
        }*/

        //bind the views
        /*appLogo = (ImageView) findViewById(R.id.applogo_imageView);
        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameSignup = (EditText) findViewById(R.id.userNameSignUp);
        passwordSignup = (EditText) findViewById(R.id.passwordSignUp);
        repeatPasswordSignup = (EditText) findViewById(R.id.repeatPasswordSignUp);
        emailSignup = (EditText) findViewById(R.id.emailSignUp);*/

        //Set app logo
        /*if(config.getAppLogo() != 0) {
            appLogo.setImageResource(config.getAppLogo());
        } else {
            appLogo.setVisibility(View.GONE);
        }*/

        //Facebook login callback
        callbackManager = CallbackManager.Factory.create();
    }

    private void setOnClickListeners() {
        if (signInButton != null) {
            signInButton.setOnClickListener(this);
        }

        if (signUpButton != null) {
            signUpButton.setOnClickListener(this);
        }

        if (facebookButton != null) {
            facebookButton.setOnClickListener(this);
        }

        if (googleButton != null) {
            googleButton.setOnClickListener(this);
        }
    }

    private void bindViews(View rootView) {
        signInButton = (Button) rootView.findViewWithTag(SmartLoginConfig.CUSTOM_LOGIN_BUTTON_ID);
        signUpButton = (Button) rootView.findViewWithTag(SmartLoginConfig.CUSTOM_SIGNUP_BUTTON_ID);
        facebookButton = (Button) rootView.findViewWithTag(SmartLoginConfig.FACEBOOK_BUTTON_ID);
        googleButton = (Button) rootView.findViewWithTag(SmartLoginConfig.GOOGLE_BUTTON_ID);
        emailEdittext = (EditText) rootView.findViewWithTag(SmartLoginConfig.EMAIL_EDITTEXT_TAG_ID);
        passwordEdittext = (EditText) rootView.findViewWithTag(SmartLoginConfig.PASSWORD_EDITTEXT_TAG_ID);
    }

    // Handling results of Facebook and Google login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) {
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
                Log.d("GOOGLE SIGN IN", "" + requestCode);
                // Signed out, show unauthenticated UI.
                progress.dismiss();
                Toast.makeText(SmartLoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show();
                finishLogin(null);
            }
        } else {
            //for facebook login
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (progress != null) {
            progress.dismiss();
        }
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
        //int id = view.getId();
        String tag = (String) view.getTag();
        switch (tag) {
            case SmartLoginConfig.FACEBOOK_BUTTON_ID:
                //doFacebookLogin();
                new FacebookLogin().with(callbackManager).handleLoginRequest(this, config);
                break;
            case SmartLoginConfig.GOOGLE_BUTTON_ID:
                //doGoogleLogin();
                new GoogleLogin().with(mGoogleApiClient).handleLoginRequest(this, config);
                break;
            case SmartLoginConfig.CUSTOM_LOGIN_BUTTON_ID:
                //doCustomSignin();
                new CustomLogin().with(emailEdittext, passwordEdittext).handleLoginRequest(this, config);
                break;
            case SmartLoginConfig.CUSTOM_SIGNUP_BUTTON_ID:
                //doCustomSignup();
                new CustomSignUp().handleLoginRequest(this, config);
                break;
            default:
                break;
        }
    }

    public void finishLogin(SmartUser smartUser) {
        if (smartUser != null) {
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
                DialogUtil.getErrorDialog(R.string.network_error, this).show();
                finish();
            }
        } else {
            DialogUtil.getErrorDialog(R.string.login_failed, this).show();
            //finish();
        }
    }
}
