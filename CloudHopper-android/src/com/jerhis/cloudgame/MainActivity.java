package com.jerhis.cloudgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;

public class MainActivity extends AndroidApplication implements PlatformInterface, GameHelper.GameHelperListener {

    final String leaderboardID = "CgkIpdXK2pEUEAIQAQ",
                bannerID = "ca-app-pub-7112170935668014/4879599282",
                interstitialID = "ca-app-pub-7112170935668014/9309798883",
                achievementID = "";
    MyGdxGame game;
    GameHelper gameHelper;
    AdView adView;
    AdRequest adRequest;
    RelativeLayout layout;
    View gameView;
    RelativeLayout.LayoutParams adParamsMenu, adParamsGame;
    InterstitialAd interstitialAd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        // Do the stuff that initialize() would do for you, make a game view
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        game = new MyGdxGame(this);
        gameView = initializeForView(game, cfg);

        gameHelperSetup();

        adSetup();

        //add views to the layout
        layout = new RelativeLayout(this);
        layout.addView(gameView);
        //layout.addView(adView, adParams);
        setContentView(layout);
    }

    //---------------------WAKELOCK MESSENGER ----------------------------
    public void wakeLockMessenger(int k) {
        if (k == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d("cloudgame", "we got a 1 here");
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });
        }
        else if (k == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d("cloudgame", "we got a 0 here");
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });
        }
    }

    //-------------------ADS STUFF-----------------------------------------
    public void adSetup() {
        //Make the view to hold the ads
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(bannerID);
        //add the parameters for the ad's location
        adParamsMenu = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParamsMenu.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParamsMenu.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        adParamsGame = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParamsGame.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParamsGame.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //make a request for the ad
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E5C749878442D4EF6615C198BC8901AC")
                .addTestDevice("EC0AE7B4F95CDA0D135E49F10E00F037")
                .build();
        adView.loadAd(adRequest);
        adView.setBackgroundColor(Color.TRANSPARENT);
        //make an interstitial ad object
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(interstitialID);
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public int adMessenger(int k, int last) {
        Log.d("jerhis", "ADS: " + k + " / " + last);

        if (k == 2 || k == 5 && last == 6 || last == 5 && k == 6)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.removeView(adView);
                }
            });

        if (k == 5 || k == 6) {
            final int kk = k;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.addView(adView, kk == 5 ? adParamsMenu : adParamsGame);
                }
            });
        }
        else if (k == 3) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                }
            });
        }
        else if (k == 4) {
            final Context context = this;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    interstitialAd = new InterstitialAd(context);
                    interstitialAd.setAdUnitId(interstitialID);
                    interstitialAd.loadAd(adRequest);
                }
            });
        }

        return 0;
    }

    //-------------------SIGN IN/ OUT-------------------------------------
    public void gameHelperSetup() {
        //deal with gamehelper
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.setMaxAutoSignInAttempts(0);
        //gameHelper.enableDebugLog(true);
        gameHelper.setup(this);
    }

    @Override
    public int googleLogin(int in1out2) {
        Log.d("jerhis", "SIGn: " + in1out2);

        if (in1out2 == 1) {
            //sign in
            if (!gameHelper.isSignedIn()) {
                gameHelper.beginUserInitiatedSignIn();
            }
        }
        else if (in1out2 == 2) {
            //sign out
            if (gameHelper.isSignedIn()) {
                gameHelper.signOut();
                game.loggedInToGoogle = false;
            }
        }

        return 0;
    }

    @Override
    public void onSignInFailed() {
        Log.d("jerhis", "SIGN FAILED");
        game.loggedInToGoogle = false;
    }

    @Override
    public void onSignInSucceeded() {
        Log.d("jerhis", "SIGN SUCCEEDED");
        game.loggedInToGoogle = true;
        game.tryToSave();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }
    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        gameHelper.onActivityResult(request, response, data);
    }

    //-------------------LEADERBOARD---------------------------------------
    @Override
    public int leaderboard(int score) {
        Log.d("jerhis", "LEADERBOARD: " + score);

        if (score > 0) {
            if (gameHelper.isSignedIn())
                Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardID, score);
            else
                game.failedLeaderboard(score);
        }
        else if (score == -1) {
            //show the leaderboard
            if (gameHelper.isSignedIn())
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), leaderboardID), 0);
        }

        return 0;
    }


    //-------------------ACHIEVEMENTS--------------------------------------
    @Override
    public int achievement(int k) {
        Log.d("jerhis", "ACHIEVEMENT: " + k);

        //Games.Achievements.unlock(getApiClient(), "my_achievement_id");
        //Games.Achievements.increment(getApiClient(), "my_incremental_achievment_id", 1);
        //startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);

        if (k == -1) {
            if (gameHelper.isSignedIn()) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 0);
            }
        }
        else if (k >= 0) {
            if (gameHelper.isSignedIn()) {
                switch (k) {
                    case 0: break;
                    case 1: break;
                    case 2: break;
                    case 3: break;
                    case 4: break;
                    case 5: break;
                    case 6: break;
                }
            }
            else {
                game.failedAchievement(k);
            }
            /*if (k >= 40000) Games.Achievements.unlock(gameHelper.getApiClient(), "CggI7qKfjjsQAhAF");
            if (k >= 100000) Games.Achievements.unlock(gameHelper.getApiClient(), "CggI7qKfjjsQAhAG");
            if (k >= 700000) Games.Achievements.unlock(gameHelper.getApiClient(), "CggI7qKfjjsQAhAH");
            if (k == -1) {
                if (gameHelper.isSignedIn()) {
                    Games.Achievements.increment(gameHelper.getApiClient(), "CggI7qKfjjsQAhAD", 1);
                    Games.Achievements.increment(gameHelper.getApiClient(), "CggI7qKfjjsQAhAE", 1);
                }
                else {
                    game.incrementUnsaved();
                }
            }
            else if (k < -3 && gameHelper.isSignedIn()) {
                Games.Achievements.increment(gameHelper.getApiClient(), "CggI7qKfjjsQAhAD", -3-k);
                Games.Achievements.increment(gameHelper.getApiClient(), "CggI7qKfjjsQAhAE", -3-k);
            }*/
        }

        return 0;
    }

}

//ca-app-pub-7112170935668014/4879599282  Banner1
//ca-app-pub-7112170935668014/9309798883  Interstitial

/*CHECKLIST FOR COPYCATS
-xml file stuffs
-libraries stuff
-res stuff - strings, launcher icons
-assets
-classes - main, game utils, ect...


<?xml version="1.0" encoding="UTF-8"?>

-<manifest android:versionName="1.0" android:versionCode="1" package="com.jerhis.lemonjerhis" xmlns:android="http://schemas.android.com/apk/res/android">

<uses-sdk android:targetSdkVersion="17" android:minSdkVersion="5"/>


-<application android:label="@string/app_name" android:icon="@drawable/ic_launcher">


-<activity android:label="@string/app_name" android:configChanges="keyboard|keyboardHidden|orientation|screenSize" android:screenOrientation="portrait" android:name="com.jerhis.lemonjerhis.MainActivity">


-<intent-filter>

<action android:name="android.intent.action.MAIN"/>

<category android:name="android.intent.category.LAUNCHER"/>

</intent-filter>

</activity>

<activity android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" android:name="com.google.android.gms.ads.AdActivity"> </activity>

<meta-data android:name="com.google.android.gms.version" android:value="4323000"/>

<meta-data android:name="com.google.android.gms.games.APP_ID" android:value="@string/app_id"/>

<meta-data android:name="com.google.android.gms.appstate.APP_ID" android:value="@string/app_id"/>

</application>

<uses-permission android:name="android.permission.INTERNET"/>

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

</manifest>




?xml version="1.0" encoding="UTF-8"?>

-<resources>

<string name="app_name">LemonRiser</string>

<string name="gamehelper_sign_in_failed">Failed to sign in. Please check your network connection and try again.</string>

<string name="gamehelper_app_misconfigured">The application is incorrectly configured. Check that the package name and signing certificate match the client ID created in Developer Console. Also, if the application is not yet published, check that the account you are trying to sign in with is listed as a tester account. See logs for more information.</string>

<string name="gamehelper_license_failed">License check failed.</string>

<string name="gamehelper_unknown_error">Unknown error.</string>

</resources>




<?xml version="1.0" encoding="UTF-8"?>

-<resources>

<string name="app_id">15867564398</string>

<string name="best_scores_leaderboard">CggI7qKfjjsQAhAB</string>

</resources>
 */
