[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20Smart%20Login-green.svg?style=true)](https://android-arsenal.com/details/1/3026)

![Image](https://raw.githubusercontent.com/CodelightStudios/Android-Smart-Login/master/Screenshots/Info_new.png)

# What's in the box

- The login framework for your app
- Implementation of **Facebook** and **Google** login
- Easy way to implement custom login and sign up

# Setup
## 1. Include in your project

### Using Gradle
The **Android-Smart-Login** library is pushed to jcenter, so you need to add the following dependency to your app's `build.gradle`.

```gradle
compile 'codelight.studios:android-smart-login:1.2'
```

### As a module
If you can't include it as gradle dependency, you can also download this GitHub repo and copy the library folder to your project.


## 2. Usage

First step in configuring the Smart Login Framework is to implement `SmartLoginCallbacks` in your Activity.

```java
public interface SmartLoginCallbacks {
    void onLoginSuccess(SmartUser user);
    void onLoginFailure(SmartLoginException e);
    SmartUser doCustomLogin();
    SmartUser doCustomSignup();
}
```

Next step is to configure the `SmartLoginConfig`.

Example:

```java
SmartLoginConfig config = new SmartLoginConfig(this /* Context */, this /* SmartLoginCallbacks */);
config.setFacebookAppId(getString(R.string.facebook_app_id));
```
This is the simplest way to configure the library to enable Custom login mode along with `Facebook` and `Google` login modes.

Next step is to override the `onActivityResult` of your Activity.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    smartLogin.onActivityResult(requestCode, resultCode, data, config);
}
```

Final step is to call the `login` method when user clicks on the login button.

```java
/* Facebook Login */
SmartLogin smartLogin = SmartLoginFactory.build(LoginType.Facebook);
smartLogin.login(config);

/* Google Login */
SmartLogin smartLogin = SmartLoginFactory.build(LoginType.Google);
smartLogin.login(config);
```
**That's it!**

Once the login succeeds, SmartUser object is returned in `onLoginSuccess(SmartUser user)`.

For detailed usage and examples check the **[Project's Wiki](https://github.com/CodelightStudios/Android-Smart-Login/wiki)**

# Included Libraries
The following third-party libraries were used in this framework.

- Facebook SDK
- Google Play Services - Auth
- GSON library

# Other Features
Get the current logged in user at anytime from your application by just calling **UserSessionManager.getCurrentUser** method.

```java
SmartUser currentUser = UserSessionManager.getCurrentUser(context);
if(currentUser != null){
    //You have got what you need
}
```
# Contribution
All contributions are welcome. Encounter any issue? Don't hesitate to [open an issue](https://github.com/CodelightStudios/Android-Smart-Login/issues)

Convention: **Master branch** would be the development branch. So feel free to fork from the Master branch. **Release branch** will be merged with master branch after every major release.

# Our other libraries
### [Weather Downloader library](https://github.com/CodelightStudios/Weather-Downloader)
An easy and efficient way to get weather information into your app.

# License

    Copyright 2017 Codelight Studios

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
