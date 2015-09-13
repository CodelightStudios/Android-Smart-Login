package studios.codelight.smartlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import studios.codelight.smartloginlibrary.SmartLoginBuilder;
import studios.codelight.smartloginlibrary.SmartLoginHelper;


public class MainActivity extends AppCompatActivity {
    //SmartFacebookResult smartFacebookResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartLoginBuilder loginBuilder = new SmartLoginBuilder();
        SmartLoginHelper loginHelper = new LoginHelper();

        startActivity(loginBuilder.with(this)
                .isTwitterLoginEnabled(true)
                .isFacebookLoginEnabled(true)
                .isGoogleLoginEnabled(false)
                .setCustomLoginHelper(loginHelper)
                .build());

//        Intent intent = new Intent(this, SmartLoginActivity.class);
//        startActivity(intent);
//        smartFacebookResult.getAccessToken().getUserId();
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
