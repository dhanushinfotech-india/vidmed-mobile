package org.telemedicine;

import android.app.Application;
import android.content.Context;

/**
 * Created by Naveen.k on 9/9/2016.
 */
public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

}
