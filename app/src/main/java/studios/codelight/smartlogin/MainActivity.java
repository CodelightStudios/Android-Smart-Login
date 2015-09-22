package studios.codelight.smartlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import studios.codelight.smartloginlibrary.SmartCustomLoginHelper;
import studios.codelight.smartloginlibrary.SmartLoginBuilder;


public class MainActivity extends AppCompatActivity {
    //SmartFacebookResult smartFacebookResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartLoginBuilder loginBuilder = new SmartLoginBuilder();
                SmartCustomLoginHelper loginHelper = new SmartCustomLoginHelper() {
                    @Override
                    protected void customSignin() {
                        Log.d("Custom", "Custom sign in");
                    }

                    @Override
                    protected void customSignup() {
                        Log.d("Custom", "Custom sign up");
                    }
                };

                startActivity(loginBuilder.with(MainActivity.this)
                        .isFacebookLoginEnabled(true)
                        .isGoogleLoginEnabled(false)
                        .setCustomLoginHelper(loginHelper)
                        .build());
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
}
