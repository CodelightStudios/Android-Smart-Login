package studios.codelight.smartloginlibrary;

/**
 * Created by Kalyan on 9/9/2015.
 */
public class SmartLoginConfig {
    private int appLogo;
    private boolean customLoginEnabled;
    private boolean isFacebookEnabled;
    private boolean isGoogleEnabled;
    private boolean isTwitterEnabled;

    public int getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(int appLogo) {
        this.appLogo = appLogo;
    }

    public boolean isFacebookEnabled() {
        return isFacebookEnabled;
    }

    public void setIsFacebookEnabled(boolean isFacebookEnabled) {
        this.isFacebookEnabled = isFacebookEnabled;
    }

    public boolean isGoogleEnabled() {
        return isGoogleEnabled;
    }

    public void setIsGoogleEnabled(boolean isGoogleEnabled) {
        this.isGoogleEnabled = isGoogleEnabled;
    }

    public boolean isTwitterEnabled() {
        return isTwitterEnabled;
    }

    public void setIsTwitterEnabled(boolean isTwitterEnabled) {
        this.isTwitterEnabled = isTwitterEnabled;
    }

    public boolean isCustomLoginEnabled() {
        return customLoginEnabled;
    }

    public void setCustomLoginEnabled(boolean customLoginEnabled) {
        this.customLoginEnabled = customLoginEnabled;
    }
}
