package org.telemedicine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


/**
 * Created by Naveen.k on 8/11/2016.
 */
public class SplashScreen extends SuperActivity{
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(!getStrValueFromPref("isUserloged").equalsIgnoreCase("yes")) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    gotoHome();
//                    Intent in = new Intent(SplashScreen.this, HomeActivity.class);
//
//                    startActivity(in);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onClick(View v) {

    }
}
