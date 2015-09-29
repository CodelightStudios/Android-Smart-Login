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
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.util.DialogUtil;

public class SmartLoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    CallbackManager callbackManager;
    SmartLoginConfig config;
    EditText usernameEditText, passwordEditText;
    ProgressDialog progress;


    //Google Sign in related
    private GoogleApiClient mGoogleApiClient;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the cofile object first
        Bundle bundle = getIntent().getExtras();
        config = SmartLoginConfig.unpack(bundle);

        FacebookSdk.setApplicationId(config.getFacebookAppId());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_smart_login);

        //bind the views
        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

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

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
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
            SmartGoogleUser googleUser = populateGoogleuser(currentPerson);
            progress.dismiss();
            Intent data = new Intent();
            data.putExtra(SmartLoginConfig.USER, googleUser);
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
                //showErrorDialog(connectionResult);
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
//                Intent intent = new Intent();
//                intent.putExtra("smartUser", "failed");
//                setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
//                finish();
            }
        }
//        else {
//            // Show the signed-out UI
//            //showSignedOutUI();
//        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.login_fb_button){
            //do facebook login
            doFacebookLogin();
        } else if(id == R.id.login_google_button){
            //do google login
            //Temporarily add code to logout in sign click and login again for testing
            //if (mGoogleApiClient.isConnected()) {
                //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                //Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                //mGoogleApiClient.disconnect();
            //}
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
        progress = ProgressDialog.show(this, "", "Logging in...", true);
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    private void doCustomSignup() {
//        final ProgressDialog progress = ProgressDialog.show(this, "", "Logging in...", true);
//        if(SmartLoginBuilder.mSmartCustomLoginListener != null) {
//            if(SmartLoginBuilder.mSmartCustomLoginListener.customSignup()){
//                progress.dismiss();
//                setResult(SmartLoginConfig.CUSTOM_SIGNUP_REQUEST);
//            }else {
//                setResult(RESULT_CANCELED);
//            }
//            finish();
//        }
    }

    private void doCustomSignin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(username.equals("")){
            DialogUtil.getErrorDialog(R.string.username_error, this).show();
        } else if(password.equals("")){
            DialogUtil.getErrorDialog(R.string.password_error, this).show();
        } else {
            if (SmartLoginBuilder.mSmartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", "Logging in...", true);
                if (SmartLoginBuilder.mSmartCustomLoginListener.customSignin(username, password)) {
                    progress.dismiss();
                    setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        }
    }

    private void doFacebookLogin() {
        if(config.isFacebookEnabled()) {
            Toast.makeText(SmartLoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
            final ProgressDialog progress = ProgressDialog.show(this, "", "Logging in...", true);
            LoginManager.getInstance().logInWithReadPermissions(SmartLoginActivity.this, Collections.singletonList("public_profile, email, user_birthday, user_friends"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    progress.setMessage("Getting User Info...");
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            SmartFacebookUser facebookUser = new SmartFacebookUser();
                            progress.dismiss();
                            try {
                                if(object.has(SmartLoginConfig.FacebookFields.EMAIL))
                                    facebookUser.setEmail(object.getString(SmartLoginConfig.FacebookFields.EMAIL));
                                if(object.has(SmartLoginConfig.FacebookFields.BIRTHDAY))
                                    facebookUser.setBirthday(object.getString(SmartLoginConfig.FacebookFields.BIRTHDAY));
                                if(object.has(SmartLoginConfig.FacebookFields.GENDER)) {
                                    try {
                                        SmartLoginConfig.Gender gender = SmartLoginConfig.Gender.valueOf(object.getString(SmartLoginConfig.FacebookFields.GENDER));
                                        switch (gender) {
                                            case male:
                                                facebookUser.setGender(0);
                                                break;
                                            case female:
                                                facebookUser.setGender(1);
                                                break;
                                        }
                                    }catch (Exception e){
                                        //if gender is not in the enum it is set to unspecified value (2)
                                        facebookUser.setGender(2);
                                        Log.e(getClass().getSimpleName(), e.getMessage());
                                    }
                                }
                                if(object.has(SmartLoginConfig.FacebookFields.LINK))
                                    facebookUser.setProfileLink(object.getString(SmartLoginConfig.FacebookFields.LINK));
                                if(object.has(SmartLoginConfig.FacebookFields.ID))
                                    facebookUser.setUserId(object.getString(SmartLoginConfig.FacebookFields.ID));
                                if(object.has(SmartLoginConfig.FacebookFields.NAME))
                                    facebookUser.setProfileName(object.getString(SmartLoginConfig.FacebookFields.NAME));
                                if(object.has(SmartLoginConfig.FacebookFields.FIRST_NAME))
                                    facebookUser.setFirstName(object.getString(SmartLoginConfig.FacebookFields.FIRST_NAME));
                                if(object.has(SmartLoginConfig.FacebookFields.MIDDLE_NAME))
                                    facebookUser.setMiddleName(object.getString(SmartLoginConfig.FacebookFields.MIDDLE_NAME));
                                if(object.has(SmartLoginConfig.FacebookFields.LAST_NAME))
                                    facebookUser.setLastName(object.getString(SmartLoginConfig.FacebookFields.LAST_NAME));

                                Intent intent = new Intent();
                                intent.putExtra(SmartLoginConfig.USER, facebookUser);
                                setResult(SmartLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
                                finish();
                            } catch (JSONException e) {
                                Log.e(getClass().getSimpleName(), e.getMessage());
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

    private SmartGoogleUser populateGoogleuser(Person person){
        //Create a new google user
        SmartGoogleUser googleUser = new SmartGoogleUser();
        //populate the user
        if(person.hasName()) {
            Person.Name name = person.getName();
            if (name.hasGivenName())
                googleUser.setFirstName(name.getGivenName());
            if (name.hasFamilyName())
                googleUser.setLastName(name.getFamilyName());
            if (name.hasFormatted())
                googleUser.setFullName(name.getFormatted());
            if (name.hasMiddleName())
                googleUser.setMiddleName(name.getMiddleName());
        }
        if(person.hasId())
            googleUser.setUserId(person.getId());
        if(person.hasNickname())
            googleUser.setNickname(person.getNickname());
        if(person.hasDisplayName())
            googleUser.setDisplayName(person.getDisplayName());
        if(person.hasBirthday())
            googleUser.setBirthday(person.getBirthday());
        if(person.hasAboutMe())
            googleUser.setAboutMe(person.getAboutMe());
        if(person.hasLanguage())
            googleUser.setLanguage(person.getLanguage());
        if(person.hasGender())
            googleUser.setGender(person.getGender());
        if(person.hasBraggingRights())
            googleUser.setBraggingRights(person.getBraggingRights());
        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        if(email != null){
            googleUser.setEmail(email);
        }
        //return the populated google user
        return googleUser;
    }

}
