package com.jerhis.cloudgame;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
//import com.google.ads.*;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication implements PlatformInterface {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new MyGdxGame(this), cfg);

        /*super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        //initialize(new MyGdxGame(), cfg);
        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new MyGdxGame(), cfg);

        // Create and setup the AdMob view
        AdView adView = new AdView(this, AdSize.BANNER, "ca-app-pub-7112170935668014/3221190882"); // Put in your secret key here
        adView.loadAd(new AdRequest());

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);

        // Hook it all up
        setContentView(layout);*/
    }

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

    public int adMessenger(int k) {
       return -1;
    }

}

        /*



        package com.jerhis.lrgdx2;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.google.ads.*;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        //initialize(new MyGdxGame(), cfg);
        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new MyGdxGame(), cfg);

        // Create and setup the AdMob view
        AdView adView = new AdView(this, AdSize.BANNER, "ca-app-pub-7112170935668014/3221190882"); // Put in your secret key here
        adView.loadAd(new AdRequest());

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);

        // Hook it all up
        setContentView(layout);
    }
}
         */

//ca-app-pub-7112170935668014/4879599282  Banner1
//ca-app-pub-7112170935668014/9309798883  Interstitial