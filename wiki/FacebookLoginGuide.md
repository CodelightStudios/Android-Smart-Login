#Android Smart Login Steps

##Facebook Login

###Prerequisites
Before we begin, we need to setup our application with Facebook.

- Go to [Facebook Developers Quick start guide](https://developers.facebook.com/quickstarts/) and select your App to configure (or create a new one)

![fb_import](/Users/kalyandechiraju/Desktop/Screen Shot 2016-09-25 at 11.52.23 AM.png)

- In this quick start menu, all we need is to check the `App Info` and `Key Hashes` sections.
- Add these two sections with proper data and copy your APP ID before you leave. (Rest every thing is taken care by this Library)
- Next step is to enable single sign on for your app by choosing your app from `My Apps` on the [Facebook Developer site](developers.facebook.com/apps/), choosing `Settings` for your app, and setting `Single Sign On` to `Yes`.

###Steps to add Facebook Login to your app

- Add the below code in `AndroidManifest.xml` of your App

```xml
<activity 
	android:name="com.facebook.FacebookActivity"
	android:configChanges=
                 "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
	android:theme="@android:style/Theme.Translucent.NoTitleBar"
	android:label="@string/app_name" />
```
-dsjbf